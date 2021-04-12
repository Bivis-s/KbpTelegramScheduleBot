package bot.parser;

import by.bivis.kbp.parser.objects.News;
import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.parser.Parser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BotParser implements Parser<News, Source, Schedule> {
    private KbpDbLinkedParser parser;

    @Override
    public List<News> getNews() {
        return parser.getNews();
    }

    @Override
    public Schedule getTodayAndTomorrowSchedule(Source source) {
        return parser.getSchedule(source);
    }

    @Override
    public Schedule getExtendedSchedule(Source source) {
        return parser.getSchedule(source);
    }

    @Override
    public List<Source> getSources() {
        return parser.getSources();
    }
}
