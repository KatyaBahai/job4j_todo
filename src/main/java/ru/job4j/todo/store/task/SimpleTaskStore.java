package ru.job4j.todo.store.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class SimpleTaskStore implements TaskStore {
    private final CrudRepository cr;

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Task WHERE id = :id";
        int affectedRows = cr.executeDeleteOrUpdate(sql, Map.of("id", id));
        return affectedRows > 0;
    }

    @Override
    public List<Task> findPendingTasks() {
        return cr.query("SELECT DISTINCT t from Task t JOIN FETCH t.priority join FETCH t.categories WHERE t.done = false ORDER BY t.id", Task.class);
    }

    @Override
    public List<Task> findCompletedTasks() {
        return cr.query("SELECT DISTINCT t from Task t JOIN FETCH t.priority join FETCH t.categories WHERE t.done = true  ORDER BY t.id", Task.class);
    }

    @Override
    public Optional<Task> add(Task task) {
        try {
            cr.run(session -> session.save(task));
            return Optional.of(task);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Task> findAll() {
        return cr.query("SELECT DISTINCT t from Task t JOIN FETCH t.priority join FETCH t.categories ORDER BY t.id", Task.class);
    }

    @Override
    public Optional<Task> findById(int id) {
        return cr.optional("from Task t JOIN FETCH t.priority join FETCH t.categories WHERE t.id = :id", Task.class, Map.of("id", id));
    }

    @Override
    public Optional<Task> edit(Task task) {
        try {
            cr.run(session -> session.merge(task));

            return Optional.of(task);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean editDone(int id) {
        return cr.executeDeleteOrUpdate(
                "UPDATE Task t SET t.done = CASE WHEN t.done = true THEN false ELSE true END WHERE t.id = :id",
                Map.of("id", id)) > 0;
    }
}
