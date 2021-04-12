package bot.db.subscription;

import bot.db.daos.KbpSourceDao;
import bot.db.daos.TelegramUserDao;
import bot.db.user.TelegramUser;
import by.bivis.kbp.parser.objects.Source;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "source_id")
    private long sourceId;

    public TelegramUser getUser(TelegramUserDao dao) {
        return dao.get(getUserId());
    }

    public Source getSource(KbpSourceDao dao) {
        return dao.get(getSourceId());
    }
}
