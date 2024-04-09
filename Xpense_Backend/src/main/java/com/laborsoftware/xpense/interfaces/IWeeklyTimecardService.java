package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.WeeklyTimecard;

import java.util.List;
import java.util.Optional;

public interface IWeeklyTimecardService {
    WeeklyTimecard save(WeeklyTimecard weeklyTimecard);

    void delete(Long id);

    List<WeeklyTimecard> findAll();

    Optional<WeeklyTimecard> findOne(Long id);
}
