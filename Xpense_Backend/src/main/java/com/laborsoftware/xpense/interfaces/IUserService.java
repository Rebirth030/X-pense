package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User save(User user);

    void delete(Long id);

    List<User> findAll();

    Optional<User> findOne(Long id);
}
