package bot.models;

import bot.db.objects.Note;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.enums.UserState;
import by.bivis.schedule_bot_model.models.ScheduleBotModel;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BotModel extends ScheduleBotModel<TelegramUser, News, Source, Schedule, Note> {

    @Override
    public TelegramUser setStateToUser(TelegramUser user, UserState state) {
        user.setState(state);
        return user;
    }

    @Override
    public TelegramUser setSelectedSourceCategoryToUser(TelegramUser user, String sourceCategory) {
        user.setSelectedSourceCategory(sourceCategory);
        return user;
    }

    @Override
    public TelegramUser setSelectedSourceSubcategoryToUser(TelegramUser user, String sourceSubcategory) {
        user.setSelectedSourceSubcategory(sourceSubcategory);
        return user;
    }

    @Override
    public long getUserId(TelegramUser user) {
        return user.getId();
    }

    @Override
    public long getSourceId(Source source) {
        return source.getId();
    }

    @Override
    public TelegramUser setSelectedSourceCategoryAndSubcategoryToNull(TelegramUser user) {
        user.setSelectedSourceCategory(null);
        user.setSelectedSourceSubcategory(null);
        return user;
    }

    @Override
    protected TelegramUser setNotifyTimeToUser(TelegramUser user, String notifyTime) {
        user.setNotifyTime(notifyTime);
        return user;
    }

    @Override
    protected Note createNoteFromText(TelegramUser user, String noteText) {
        Note note = new Note();
        note.setUserId(user.getId());
        note.setValue(noteText);
        return note;
    }
}
