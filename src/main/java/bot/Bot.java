package bot;

import bot.db.objects.Note;
import bot.db.objects.TelegramUser;
import bot.secure.Values;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.controllers.ScheduleBotController;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    private ScheduleBotController<TelegramUser, News, Source, Schedule, Note> controller;

    @Override
    public String getBotUsername() {
        return Values.botName;
    }

    @Override
    public String getBotToken() {
        return Values.botApiToken;
    }

    private TelegramUser getUser(Update update) {
        TelegramUser user = new TelegramUser();
        User telegramUser = update.getMessage().getFrom();
        user.setId(telegramUser.getId());
        user.setFirstname(telegramUser.getFirstName());
        user.setLastname(telegramUser.getLastName());
        return user;
    }

    @Override
    public void onUpdateReceived(Update update) {
        TelegramUser user = getUser(update);
        if (controller.getModel().getUserDao().get(user.getId()) == null) {
            controller.getModel().getUserDao().save(user);
        }
        controller.handle(user, update.getMessage().getText());
    }
}
