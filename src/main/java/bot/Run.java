package bot;

import bot.controllers.TelegramBotController;
import bot.db.daos.*;
import bot.models.TelegramBotModel;
import bot.parser.BotParser;
import bot.parser.KbpDbLinkedParser;
import bot.views.BotView;
import by.bivis.kbp.parser.objects.News;
import by.bivis.schedule_bot_model.objects.db_objects.NewsDao;

public class Run {
    public static void main(String[] args) {
        //TODO Внедрить уже наконец зависимости?

        Bot telegramBot = new Bot();
        BotView view = new BotView();
        view.setBot(telegramBot);

        SubscriptionDao subscriptionDao = new SubscriptionDao();

        NewsDao<News> newsDao = new KbpNewsDao();
        KbpSourceDao sourceDao = new KbpSourceDao();

        KbpScheduleDao scheduleDao = new KbpScheduleDao();

        KbpDbLinkedParser linkedParser = new KbpDbLinkedParser();
        linkedParser.setNewsDao(newsDao);
        linkedParser.setScheduleDao(scheduleDao);
        linkedParser.setSourceDao(sourceDao);

        sourceDao.setLinkedParser(linkedParser);

        TelegramUserDao userDao = new TelegramUserDao();
        userDao.setSubscriptionDao(subscriptionDao);
        userDao.setSourceDao(sourceDao);
        userDao.setNoteDao(new NoteDao());
        userDao.setNotifyDao(new NotifyDao());

        sourceDao.setSubscriptionDao(subscriptionDao);

        subscriptionDao.setSourceDao(sourceDao);
        subscriptionDao.setUserDao(userDao);

        BotParser parser = new BotParser();
        parser.setParser(linkedParser);

        TelegramBotModel model = new TelegramBotModel();
        model.setNewsDao(newsDao);
        model.setSourceDao(sourceDao);
        model.setScheduleDao(scheduleDao);
        model.setUserDao(userDao);
        model.setParser(parser);
        model.setView(view);

        TelegramBotController controller = new TelegramBotController(model);

        telegramBot.setController(controller);
        TelegramKbpScheduleBot scheduleBot = new TelegramKbpScheduleBot(controller, telegramBot);
        scheduleBot.setNotifyDao(new NotifyDao());
        scheduleBot.run();
    }
}
