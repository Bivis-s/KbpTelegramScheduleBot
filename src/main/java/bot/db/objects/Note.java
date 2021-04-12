package bot.db.objects;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "value")
    private String value;
}
