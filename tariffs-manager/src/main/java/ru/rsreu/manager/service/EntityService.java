package ru.rsreu.manager.service;

import java.util.List;

public interface EntityService<E> {
    E findById(long id);

    List<E> findAll();

    void deleteById(long id);

    E update(E e);

    boolean isUnique(E entity);

    default boolean processUpdate(E entity) {
        boolean unique = isUnique(entity);
        if (!unique) {
            return false;
        }
        update(entity);
        return true;
    }
}
