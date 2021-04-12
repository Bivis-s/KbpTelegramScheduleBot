package bot.db.hibernate_factory;

import bot.db.objects.Note;
import bot.db.objects.Subscription;
import bot.db.objects.TelegramUser;
import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.kbp.parser.objects.schedule.ScheduleCell;
import by.bivis.kbp.parser.objects.schedule.ScheduleColumn;
import by.bivis.kbp.parser.objects.schedule.ScheduleLesson;

import java.util.Arrays;
import java.util.List;

public class HibernateAnnotatedClasses {
    /**
     * Список аннотированных классов, которые есть в бд и с которыми будет работать хибернейт
     */
    public static final List<Class<?>> annotatedClasses = Arrays.asList(
            TelegramUser.class,
            Schedule.class,
            ScheduleColumn.class,
            ScheduleCell.class,
            ScheduleLesson.class,
            News.class,
            Source.class,
            Subscription.class,
            Note.class
    );

    private HibernateAnnotatedClasses() {
    }
}
