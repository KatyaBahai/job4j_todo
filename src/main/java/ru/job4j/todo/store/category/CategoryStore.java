package ru.job4j.todo.store.category;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;

public interface CategoryStore {
    Collection<Category> findAll();

    Collection<Category> findAllByIds(List<Integer> categoryIds);
}
