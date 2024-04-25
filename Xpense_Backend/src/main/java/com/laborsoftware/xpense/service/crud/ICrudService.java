package com.laborsoftware.xpense.service.crud;

import com.laborsoftware.xpense.domain.ApplicationUser;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T, ID> {
    ResponseEntity<T> save(T entity);

    ResponseEntity<T> update(T entity, ID id);

    void delete(ID id);

    ResponseEntity<List<T>> findAll();

    ResponseEntity<T> findOne(ID id);
}
