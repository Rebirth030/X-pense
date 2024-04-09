package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.UserTimecard;

import java.util.List;
import java.util.Optional;

public interface IUserTimecardService {
    UserTimecard save(UserTimecard userTimecard);

    void delete(Long id);

    List<UserTimecard> findAll();

    Optional<UserTimecard> findOne(Long id);
}
