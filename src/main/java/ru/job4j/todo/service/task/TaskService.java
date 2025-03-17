package ru.job4j.todo.service.task;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
     boolean deleteById(int id);

     Optional<Task> add(Task task);

     List<Task> findAll();

     Optional<Task> findById(int id);

     Optional<Task> edit(Task task);

     boolean editDone(int id);

     List<Task> findPendingTasks();

     List<Task> findCompletedTasks();
}
