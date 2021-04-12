package bot.db.objects;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notify")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "source_id")
    private long sourceId;

    @Column(name = "notify_time")
    private String notifyTime;
}
