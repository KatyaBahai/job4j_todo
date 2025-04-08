package ru.job4j.todo.service.task;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Task;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskService {
     boolean deleteById(int id);

     Optional<Task> add(Task task, ZoneId userZone);

     Collection<Task> findAll(ZoneId userZone);

     Optional<Task> findById(int id);

     Optional<Task> edit(Task task);

     boolean editDone(int id);

     Collection<Task> findPendingTasks(ZoneId userZone);

     Collection<Task> findCompletedTasks(ZoneId userZone);
}
