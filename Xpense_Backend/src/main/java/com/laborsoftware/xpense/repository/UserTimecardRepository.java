package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.UserTimecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTimecardRepository extends JpaRepository<UserTimecard, Long> {
}
