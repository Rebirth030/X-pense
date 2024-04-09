package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserTimecardId(Long userTimecardId);
}
