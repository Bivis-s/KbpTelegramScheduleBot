package bot.db.daos;

import bot.db.objects.Note;
import bot.db.objects.Subscription;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.schedule_bot_model.objects.db_objects.UserDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TelegramUserDao extends BaseDaoImpl<TelegramUser> implements UserDao<TelegramUser, Source, Note> {
    private SubscriptionDao subscriptionDao;
    private KbpSourceDao sourceDao;
    private NoteDao noteDao;

    @Override
    protected Class<TelegramUser> getGenericClass() {
        return TelegramUser.class;
    }

    @Override
    public String getSelectedSourceCategory(long userId) {
        return get(userId).getSelectedSourceCategory();
    }

    @Override
    public String getSelectedSourceSubcategory(long userId) {
        return get(userId).getSelectedSourceSubcategory();
    }

    @Override
    public void addSubscriptionToUser(long userId, Source source) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setSourceId(source.getId());
        subscriptionDao.save(subscription);
    }

    @Override
    public void removeSubscriptionFromUser(long userId, Source source) {
        for (Subscription subscription : subscriptionDao.getSignedSubscriptions(userId)) {
            if (subscription.getSource(sourceDao).getId() == source.getId() && subscription.getUserId() == userId) {
                subscriptionDao.delete(subscription);
            }
        }
    }

    @Override
    public boolean isThereSubscriptions(long userId) {
        List<Subscription> subscriptions = subscriptionDao.getAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isThereSuchSubscription(long userId, Source source) {
        List<Subscription> subscriptions = subscriptionDao.getAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId() == userId && subscription.getSourceId() == source.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isThereNotes(long userId) {
        return !getNotes(userId).isEmpty();
    }

    @Override
    public List<Note> getNotes(long userId) {
        List<Note> notes = new ArrayList<>();
        for (Note note : getNoteDao().getAll()) {
            if (note.getUserId() == userId) {
                notes.add(note);
            }
        }
        return notes;
    }

    @Override
    public void cleanNotes(long userId) {
        for (Note note : getNotes(userId)) {
            getNoteDao().delete(note);
        }
    }

    @Override
    public void addNoteToUser(Note note) {
        getNoteDao().save(note);
    }
}
