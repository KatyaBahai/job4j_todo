package ru.job4j.todo.store.priority;

import ru.job4j.todo.model.Priority;

import java.util.Collection;
import java.util.Optional;

public interface PriorityStore {
    Collection<Priority> findAll();

    Optional<Priority> findById(int id);
}
