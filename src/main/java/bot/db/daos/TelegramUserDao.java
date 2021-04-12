package bot.db.daos;

import bot.db.objects.Note;
import bot.db.objects.Notify;
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
    private NotifyDao notifyDao;

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
    public boolean isThereNotification(long userId, String notifyTime, Source source) {
        List<Notify> notifies = notifyDao.getAll();
        for (Notify notify : notifies) {
            if (notify.getUserId() == userId &&
                    notify.getSourceId() == source.getId() &&
                    notify.getNotifyTime().equals(notifyTime)) {
                return true;
            }
        }
        return false;
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

    @Override
    public void addNotifyToUser(long userId, String notifyTime, Source source) {
        Notify notify = new Notify();
        notify.setNotifyTime(notifyTime);
        notify.setUserId(userId);
        notify.setSourceId(source.getId());
        getNotifyDao().save(notify);
    }

    @Override
    public List<String> getUserNotifies(long userId) {
        List<String> notifies = new ArrayList<>();
        for (Notify notify : notifyDao.getAll()) {
            if (notify.getUserId() == userId) {
                notifies.add(notify.getNotifyTime() + " | " + getSourceDao().get(notify.getSourceId()).getValue());
            }
        }
        return notifies;
    }

    @Override
    public void clearNotifies(long userId) {
        for (Notify notify : notifyDao.getAll()) {
            if (notify.getUserId() == userId) {
                notifyDao.delete(notify);
            }
        }
    }
}
