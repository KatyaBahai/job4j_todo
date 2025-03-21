package ru.job4j.todo.store.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class SimpleUserStore implements UserStore {
    private final CrudRepository cr;

    @Override
    public Optional<User> save(User user) {
        try {
            cr.run(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return cr.optional("from User WHERE email = :email AND password = :password",
                User.class,
                Map.of("email", email, "password", password));
    }

    public boolean deleteById(int id) {
        int affectedRows = cr.executeDeleteOrUpdate("DELETE User WHERE id = :id", Map.of("id", id));
        return affectedRows > 0;
    }

    public Collection<User> findAll() {
            return cr.query("from User", User.class);
    }
}
