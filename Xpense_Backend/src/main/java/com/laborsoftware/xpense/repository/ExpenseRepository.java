package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByProjectId(Long id);

    List<Expense> findAllByApplicationUser(ApplicationUser applicationUser);

    @Query("SELECT e FROM Expense e WHERE DATE(e.startDateTime) = DATE(:currentDateTime) AND e.applicationUser.id = :userId")
    List<Expense> findAllByStartDateTimeIsTodayAndUserId(@Param("currentDateTime") ZonedDateTime currentDateTime, @Param("userId") Long userId);

}
