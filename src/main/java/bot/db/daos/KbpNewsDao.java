package bot.db.daos;

import by.bivis.kbp.parser.objects.News;
import by.bivis.schedule_bot_model.objects.db_objects.NewsDao;

public class KbpNewsDao extends BaseDaoImpl<News> implements NewsDao<News> {

    @Override
    protected Class<News> getGenericClass() {
        return News.class;
    }
}
