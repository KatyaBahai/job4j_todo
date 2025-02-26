package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query<Task> query = session.createQuery("DELETE FROM Task WHERE id = :id", Task.class)
                    .setParameter("id", id);
            if (query.executeUpdate() > 0) {
                deleted = true;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
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
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            if (task.getId() != null) {
                return Optional.of(task);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAll(Task task) {
        try (Session session = sf.openSession()) {
            List<Task> queryList = session.createQuery("from Task", Task.class).list();
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
        try {
            session = sf.openSession();
            session.beginTransaction();
            Task existingTask = session.get(Task.class, task.getId());
            if (existingTask == null) {
               return Optional.empty();
            }
            existingTask.setTitle(task.getTitle());
            existingTask.setCreated(task.getCreated());
            existingTask.setDescription(task.getDescription());
            existingTask.setDone(task.getDone());
            session.update(existingTask);
            session.getTransaction().commit();
            session.close();
    } catch (Exception e) {
        session.getTransaction().rollback();
        e.printStackTrace();
    }
        return Optional.of(task);
    }
}
