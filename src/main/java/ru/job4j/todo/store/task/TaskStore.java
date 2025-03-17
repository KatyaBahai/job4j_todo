package ru.job4j.todo.store.task;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskStore {
    List<Task> findNotDone();

    List<Task> findDone();

    Optional<Task> add(Task task);

    List<Task> findAll();

    List<Task> findPendingTasks();

    List<Task> findCompletedTasks();

    Optional<Task> findById(int id);

    Optional<Task> edit(Task task);

    boolean deleteById(int id);

    boolean editDone(int id);
}
