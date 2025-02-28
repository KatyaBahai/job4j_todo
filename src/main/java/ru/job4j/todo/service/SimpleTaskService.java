package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.SimpleTaskStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    SimpleTaskStore taskStore;

    public boolean deleteById(int id) {
        return taskStore.deleteById(id);
    }

    @Override
    public Optional<Task> add(Task task) {
        return taskStore.add(task);
    }

    @Override
    public List<Task> findAll() {
            return taskStore.findAll();
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    @Override
    public Optional<Task> edit(Task task) {
        return taskStore.edit(task);
    }
}
