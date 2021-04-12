package bot.db.objects;

import by.bivis.schedule_bot_model.enums.UserState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TelegramUser {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(name = "selected_source_category")
    private String selectedSourceCategory;

    @Column(name = "selected_source_subcategory")
    private String selectedSourceSubcategory;

    @Column(name = "notify_time")
    private String notifyTime;

    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", state=" + state +
                '}';
    }
}
