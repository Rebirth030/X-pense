package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T, ID> {
    T save(T entity);

    void delete(ID id);

    List<T> findAll();

    Optional<T> findOne(ID id);
}
