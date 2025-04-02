package ru.job4j.todo.store.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SimpleCategoryStore implements CategoryStore {
    private final CrudRepository cr;

    public Collection<Category> findAll() {
        return cr.query("from Category", Category.class);
    }

    public Collection<Category> findAllByIds(List<Integer> categoryIds) {
        return cr.query("from Category c WHERE c.id IN :fCategoriesId",
                Category.class,
                Map.of("fCategoriesId", categoryIds));
    }
}
