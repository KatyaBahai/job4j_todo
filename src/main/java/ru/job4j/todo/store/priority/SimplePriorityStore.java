package ru.job4j.todo.store.priority;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.store.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimplePriorityStore implements PriorityStore {
    private final CrudRepository cr;

    @Override
    public Collection<Priority> findAll() {
        return cr.query("from Priority p order by p.id", Priority.class);
    }

    @Override
    public Optional<Priority> findById(int id) {
        return cr.optional("from Priority c WHERE c.id = :fId", Priority.class, Map.of("fId", id));
    }
}
