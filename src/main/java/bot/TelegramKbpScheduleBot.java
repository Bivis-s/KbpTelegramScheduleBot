package bot;

import bot.db.objects.Note;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.ScheduleBot;
import by.bivis.schedule_bot_model.controllers.ScheduleBotController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramKbpScheduleBot extends ScheduleBot<TelegramUser, News, Source, Schedule, Note> {
    private final TelegramLongPollingBot bot;

    public TelegramKbpScheduleBot(ScheduleBotController<TelegramUser, News, Source, Schedule, Note> controller, TelegramLongPollingBot bot) {
        super(controller);
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
