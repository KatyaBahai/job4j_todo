package ru.job4j.todo.service.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.ZoneConverter;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.task.SimpleTaskStore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final SimpleTaskStore taskStore;
    private final ZoneConverter zoneConverter;

    public boolean deleteById(int id) {
        return taskStore.deleteById(id);
    }

    @Override
    public Optional<Task> add(Task task, ZoneId userZone) {
        LocalDateTime utcTime = zoneConverter.convertFromLocalTimeToUtc(userZone);
        task.setCreated(utcTime);
        return taskStore.add(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    @Override
    public Optional<Task> edit(Task task) {
        return taskStore.edit(task);
    }

    @Override
    public boolean editDone(int id) {
        return taskStore.editDone(id);
    }

    @Override
    public Collection<Task> findAll(ZoneId userZone) {
        return zoneConverter.convertToUserTimeZone(userZone, taskStore.findAll());
    }

    @Override
    public Collection<Task> findPendingTasks(ZoneId userZone) {
        return zoneConverter.convertToUserTimeZone(userZone, taskStore.findPendingTasks());
    }

    @Override
    public Collection<Task> findCompletedTasks(ZoneId userZone) {
        return zoneConverter.convertToUserTimeZone(userZone, taskStore.findCompletedTasks());
    }
}
