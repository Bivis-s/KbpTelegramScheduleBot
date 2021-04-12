package bot.db.daos;

import by.bivis.kbp.parser.objects.Source;
import by.bivis.kbp.parser.objects.schedule.Schedule;
import by.bivis.schedule_bot_model.objects.db_objects.ScheduleDao;

public class KbpScheduleDao extends BaseDaoImpl<Schedule> implements ScheduleDao<Schedule, Source> {

    @Override
    protected Class<Schedule> getGenericClass() {
        return Schedule.class;
    }

    @Override
    public Schedule get(Source source) {
        return get(source.getId());
    }
}
