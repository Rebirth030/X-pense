package com.laborsoftware.xpense.service.crud;

import com.laborsoftware.xpense.domain.ApplicationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T, ID> {
    ResponseEntity<T> save(@RequestHeader("Authorization") String token, T entity);

    ResponseEntity<T> update(@RequestHeader("Authorization") String token,T entity, ID id);

    void delete(@RequestHeader("Authorization") String token,ID id);

    ResponseEntity<List<T>> findAll(@RequestHeader("Authorization") String token);

    ResponseEntity<T> findOne(@RequestHeader("Authorization") String token, ID id);
}
