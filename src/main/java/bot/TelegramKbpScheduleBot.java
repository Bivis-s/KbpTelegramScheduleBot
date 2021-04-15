package bot;

import bot.db.daos.NotifyDao;
import bot.db.objects.Note;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.ScheduleBot;
import by.bivis.schedule_bot_model.controllers.ScheduleBotController;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

@Getter
@Setter
@Log4j2
public class TelegramKbpScheduleBot extends ScheduleBot<TelegramUser, News, Source, Schedule, Note> {
    private final TelegramLongPollingBot bot;
    private NotifyDao notifyDao;

    public TelegramKbpScheduleBot(ScheduleBotController<TelegramUser, News, Source, Schedule, Note> controller, TelegramLongPollingBot bot) {
        super(controller);
        this.bot = bot;
    }

    private Calendar getDateCalendarForNotify(int hours) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    @Override
    public void run() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // start notify threads for 12:00, 15:00 and 18:00
        int hours = 12;
        for (int i = 0; i < 3; i++) {
            Timer timer = new Timer();
            NotifySender sender = new NotifySender();
            sender.setModel(getController().getModel());
            sender.setNotifyDao(notifyDao);
            sender.setNotifyTime(hours + ":00");
            timer.schedule(
                    sender,
                    getDateCalendarForNotify(hours).getTime(),
                    24 * 60 * 60 * 1000
            );
            hours += 3;
        }
    }
}
