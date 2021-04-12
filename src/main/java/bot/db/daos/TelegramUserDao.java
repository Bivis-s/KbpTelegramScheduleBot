package bot.db.daos;

import bot.db.subscription.Subscription;
import bot.db.user.TelegramUser;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.schedule_bot_model.objects.db_objects.UserDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TelegramUserDao extends BaseDaoImpl<TelegramUser> implements UserDao<TelegramUser, Source> {
    private SubscriptionDao subscriptionDao;
    private KbpSourceDao sourceDao;

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
}
