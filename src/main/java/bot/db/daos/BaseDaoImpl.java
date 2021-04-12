package bot.db.daos;

import bot.db.hibernate_factory.HibernateSessionFactoryUtil;
import by.bivis.schedule_bot_model.objects.db_objects.BaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {

    private SessionFactory getSessionFactory() {
        return HibernateSessionFactoryUtil.getSessionFactory();
    }

    protected abstract Class<T> getGenericClass();

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        try {
            return (List<T>) getSessionFactory()
                    .openSession()
                    .createQuery("from " + getGenericClass().getSimpleName())
                    .list();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Override
    public T get(long entityId) {
        try {
            return getSessionFactory()
                    .openSession()
                    .get(getGenericClass(), entityId);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Override
    public void save(T entity) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void delete(T entity) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void saveOrUpdate(T entity) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteAll() {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.createQuery("delete from " + getGenericClass().getSimpleName()).executeUpdate();
            session.getTransaction().commit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
