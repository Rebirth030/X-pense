package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.WeeklyTimecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyTimecardRepository extends JpaRepository<WeeklyTimecard, Long> {
}
