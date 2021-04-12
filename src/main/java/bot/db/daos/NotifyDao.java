package bot.db.daos;

import bot.db.objects.Notify;

public class NotifyDao extends BaseDaoImpl<Notify> {
    @Override
    protected Class<Notify> getGenericClass() {
        return Notify.class;
    }
}
