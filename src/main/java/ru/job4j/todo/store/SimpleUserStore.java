package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class SimpleUserStore implements UserStore {
    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            if (user.getId() != null) {
                return Optional.of(user);
            }
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
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Session session = sf.openSession()) {
            Query<User> query = session.createQuery("from User WHERE email = :email AND password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password);
                return Optional.of(query.uniqueResult());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean deleteById(int id) {
        boolean deleted = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
            deleted = true;
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

    public Collection<User> findAll() {
        try (Session session = sf.openSession()) {
            List<User> queryList = session.createQuery("from User", User.class).list();
            return queryList;
        }
    }

}
