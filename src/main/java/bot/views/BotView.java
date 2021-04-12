package bot.views;

import bot.db.user.TelegramUser;
import bot.values.Commands;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.views.ScheduleBotView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static bot.utils.KeyboardGenerator.createReplyKeyboardMarkup;
import static bot.utils.ScheduleFormatter.formatExtendedSchedule;
import static bot.utils.ScheduleFormatter.formatTodayAndTomorrowSchedule;
import static bot.utils.TelegramBotUtils.createMessage;
import static bot.utils.TelegramBotUtils.createNewsMediaGroup;

@NoArgsConstructor
@Getter
@Setter
public class BotView implements ScheduleBotView<TelegramUser, News, Schedule, Source> {
    private TelegramLongPollingBot bot;

    public void sendMessage(TelegramUser user, String message) {
        try {
            bot.execute(createMessage(user.getId(), message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(TelegramUser user, String message, ReplyKeyboardMarkup replyMarkup) {
        try {
            bot.execute(createMessage(user.getId(), message, replyMarkup));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNews(TelegramUser user, List<News> news) {
        try {
            bot.execute(createNewsMediaGroup(user.getId(), news, 5));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSourceCategories(TelegramUser user, Set<String> sources) {
        try {
            bot.execute(createMessage(user.getId(), "Выберите категорию источника",
                    createReplyKeyboardMarkup(3, new ArrayList<>(sources))));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSourcesSubcategoryByCategory(TelegramUser user, Set<String> sourceSubcategories) {
        try {
            bot.execute(createMessage(user.getId(), "Выберите первую букву источника",
                    createReplyKeyboardMarkup(5, new ArrayList<>(sourceSubcategories))));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createStandardReplyMarkup() {
        return createReplyKeyboardMarkup(2,
                Arrays.asList(Commands.GET, Commands.GET_ALL, Commands.SUBSCRIBE, Commands.SEE));
    }

    @Override
    public void sendTodayAndTomorrowSchedule(TelegramUser user, Schedule schedule) {
        try {
            bot.execute(createMessage(user.getId(),
                    formatTodayAndTomorrowSchedule(schedule), createStandardReplyMarkup()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendExtendedSchedule(TelegramUser user, Schedule schedule) {
        try {
            bot.execute(createMessage(user.getId(), formatExtendedSchedule(schedule), createStandardReplyMarkup()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSources(TelegramUser user, List<Source> sources) {
        List<String> sourceNames = new ArrayList<>();
        for (Source source : sources) {
            sourceNames.add(source.getValue());
        }

        try {
            bot.execute(createMessage(user.getId(), "Выберите источник",
                    createReplyKeyboardMarkup(5, new ArrayList<>(sourceNames))));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInfoMessage(TelegramUser user) {
        sendMessage(user, "Привет!\n" +
                        "Я бот Колледжа Бизнеса и Права и вот что я умею:\n" +
                        Commands.INFO + " или " + Commands.INFO_DIM + " - вы здесь\n" +
                        Commands.SUBSCRIBE + " или " + Commands.SUBSCRIBE_DIM + " - подписка на расписания\n" +
                        Commands.GET + " или " + Commands.GET_DIM + " - расписание на сегодня и завтра из подписки\n" +
                        Commands.GET_ALL + " или " + Commands.GET_ALL_DIM + " - расписание на сегодня и завтра из подписки\n" +
                        Commands.REMOVE + " или " + Commands.REMOVE_DIM + " - удаление подписки\n" +
                        Commands.SEE + " - расписание без подписки\n" +
                        Commands.NOTIFY + " - настройка системы рассылки расписаний\n" +
                        Commands.NEWS + " - последние новостей коллежа\n" +
                        Commands.NOTES + " - заметки",
                createReplyKeyboardMarkup(3, Arrays.asList(
                        Commands.INFO,
                        Commands.SUBSCRIBE,
                        Commands.GET,
                        Commands.GET_ALL,
                        Commands.REMOVE,
                        Commands.SEE,
                        Commands.NOTIFY,
                        Commands.NEWS,
                        Commands.NOTES
                )));
    }

    @Override
    public void sendParsingInProcessMessage(TelegramUser user) {
        sendMessage(user, "Происходит обновление данных, может потребоваться время...");
    }

    @Override
    public void sendThereIsAlreadySuchSubscriptionMessage(TelegramUser user) {
        sendMessage(user, "Вы уже подписаны на этот источник", createStandardReplyMarkup());
    }

    @Override
    public void sendThereIsNoSuchSubscriptionMessage(TelegramUser user) {
        sendMessage(user, "Вы не подписаны на этот источник", createStandardReplyMarkup());
    }

    @Override
    public void sendThereIsNoSuchSourceMessage(TelegramUser user) {
        sendMessage(user, "Такой источник не найден", createStandardReplyMarkup());
    }

    @Override
    public void sendSubscriptionSuccessMessage(TelegramUser user) {
        sendMessage(user, "Подписка прошла успешно", createStandardReplyMarkup());
    }

    @Override
    public void sendSubscriptionRemoveWasSuccessfulMessage(TelegramUser user) {
        sendMessage(user, "Подписка удалена успешно",
                createReplyKeyboardMarkup(2, Arrays.asList(
                        Commands.GET,
                        Commands.GET_ALL,
                        Commands.SUBSCRIBE,
                        Commands.SEE,
                        Commands.REMOVE
                )));
    }

    @Override
    public void sendUserHasNoSubscriptionsMessage(TelegramUser user) {
        sendMessage(user, "Ваши подписки пусты", createStandardReplyMarkup());
    }
}
