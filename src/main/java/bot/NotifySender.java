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
import lombok.extern.log4j.Log4j2;

import java.util.TimerTask;

@Getter
@Setter
@Log4j2
public class NotifySender extends TimerTask {
    private String notifyTime;
    private ScheduleBotModel
            <TelegramUser, News, Source, Schedule, Note> model;
    private NotifyDao notifyDao;

    @Override
    public void run() {
        for (Notify notify : notifyDao.getAll()) {
            log.info("Inner notify time: " + notify.getNotifyTime() + " time that set: " + notifyTime);
            if (notify.getNotifyTime().equals(notifyTime)) {
                model.sendTodayAndTomorrowScheduleToView(model.getUserDao().get(notify.getUserId()),
                        getModel().getSourceDao().get(notify.getSourceId()));
            }
        }
    }
}
