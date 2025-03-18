package ru.job4j.todo.store.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class SimpleTaskStore implements TaskStore {
    private final SessionFactory sf;

    @Override
    public boolean deleteById(int id) {
        boolean deleted = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
           int affectedRows =  session.createQuery("DELETE FROM Task WHERE id = :id")
                    .setParameter("id", id)
                   .executeUpdate();
                deleted = affectedRows > 0;
            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return deleted;
    }

    @Override
    public List<Task> findNotDone() {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task WHERE done = false", Task.class).getResultList();
            return queryList;
        }
    }

    @Override
    public List<Task> findDone() {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task WHERE done = true", Task.class).list();
            return queryList;
        }
    }

    @Override
    public Optional<Task> add(Task task) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.save(task);
            tx.commit();
            return Optional.of(task);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task", Task.class).list();
            return queryList;
        }
    }

    @Override
    public List<Task> findPendingTasks() {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task WHERE done = false", Task.class).list();
            return queryList;
        }
    }

    @Override
    public List<Task> findCompletedTasks() {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task WHERE done = true", Task.class).list();
            return queryList;
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try (Session session = sf.openSession()) {
            Query<Task> query = session.createQuery("from Task WHERE id = :id", Task.class)
                    .setParameter("id", id);
            return Optional.of(query.uniqueResult());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Task> edit(Task task) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            Query taskQuery = session
                    .createQuery("UPDATE Task t SET t.title = :title, t.description = :description"
                            + " WHERE t.id = :id")
                    .setParameter("title", task.getTitle())
                    .setParameter("description", task.getDescription())
                    .setParameter("id", task.getId());
            int updatedRows = taskQuery.executeUpdate();
            if (updatedRows <= 0) {
               return Optional.empty();
            }
            tx.commit();
    } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Optional.of(task);
    }

    @Override
    public boolean editDone(int id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            Query taskQuery =  session.createNativeQuery(
                            "UPDATE tasks SET done = NOT done WHERE id = :id")
                    .setParameter("id", id);
            int updatedRows = taskQuery.executeUpdate();
            tx.commit();
            return updatedRows > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }
}
