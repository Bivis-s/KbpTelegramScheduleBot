package bot.db.daos;

import bot.db.subscription.Subscription;
import bot.db.user.TelegramUser;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.schedule_bot_model.objects.db_objects.SourceDao;
import by.bivis.schedule_bot_model.objects.db_objects.UserDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionDao extends BaseDaoImpl<Subscription> {
    private SourceDao<Source> sourceDao;
    private UserDao<TelegramUser, Source> userDao;

    @Override
    protected Class<Subscription> getGenericClass() {
        return Subscription.class;
    }

    public List<Subscription> getSignedSubscriptions(long userId) {
        List<Subscription> subscriptions = new ArrayList<>();
        for (Subscription subscription : getAll()) {
            if (subscription.getUserId() == userId) {
                subscriptions.add(subscription);
            }
        }
        return subscriptions;
    }
}
