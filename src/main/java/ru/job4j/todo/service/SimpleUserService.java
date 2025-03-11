package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.SimpleUserStore;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    SimpleUserStore userStore;

    @Override
    public Optional<User> save(User user) {
        return userStore.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userStore.findByEmailAndPassword(email, password);
    }
}
