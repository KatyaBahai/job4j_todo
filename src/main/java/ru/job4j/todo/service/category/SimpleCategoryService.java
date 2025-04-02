package ru.job4j.todo.service.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.category.SimpleCategoryStore;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {
    private final SimpleCategoryStore simpleCategoryStore;

    @Override
    public Collection<Category> findAll() {
        return simpleCategoryStore.findAll();
    }

    @Override
    public Collection<Category> findAllByIds(List<Integer> categoryIds) {
        return simpleCategoryStore.findAllByIds(categoryIds);
    }
}
