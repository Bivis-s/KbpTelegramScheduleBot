package bot;

import bot.db.daos.NotifyDao;
import bot.db.objects.Note;
import bot.db.objects.Notify;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.models.ScheduleBotModel;
import lombok.Getter;
import lombok.Setter;

import java.util.TimerTask;

@Getter
@Setter
public class NotifySender extends TimerTask {
    private String notifyTime;
    private ScheduleBotModel
            <TelegramUser, News, Source, Schedule, Note> model;
    private NotifyDao notifyDao;

    @Override
    public void run() {
        System.out.println(notifyTime);
        for (Notify notify : notifyDao.getAll()) {
            if (notify.getNotifyTime().equals(notifyTime)) {
                model.sendTodayAndTomorrowScheduleToView(model.getUserDao().get(notify.getUserId()),
                        getModel().getSourceDao().get(notify.getSourceId()));
            }
        }
    }
}
