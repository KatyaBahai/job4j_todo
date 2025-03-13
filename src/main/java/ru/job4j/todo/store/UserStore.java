package ru.job4j.todo.store;

import ru.job4j.todo.model.User;
import java.util.Optional;

public interface UserStore {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> save(User user);

}
