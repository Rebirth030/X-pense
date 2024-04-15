package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements ICrudService<ApplicationUser, Long> {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {
        logger.debug("Request to save User {} ", applicationUser);
        ApplicationUser result = null;
        try {
            result = userRepository.save(applicationUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete User {} ", id);
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<ApplicationUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<ApplicationUser> findOne(Long id) {
        return userRepository.findById(id);
    }

    public Optional<ApplicationUser> findUserByUserTimecardId(Long userTimecardId) {
        return userRepository.findByUserTimecardId(userTimecardId);
    }
}
