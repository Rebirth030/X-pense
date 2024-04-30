package com.laborsoftware.xpense.repository;

import com.laborsoftware.xpense.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUserTimecardId(Long userTimecardId);

    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByPassword(String password);

    Optional<ApplicationUser> findByUsernameAndPassword(String username, String password);

    Optional<ApplicationUser> findByToken(String token);
}
