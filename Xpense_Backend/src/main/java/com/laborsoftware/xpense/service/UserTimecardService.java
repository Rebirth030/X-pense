package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.repository.UserTimecardRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTimecardService implements ICrudService<UserTimecard, Long> {

    Logger logger = LoggerFactory.getLogger(UserTimecardService.class);

    private final UserTimecardRepository userTimecardRepository;

    public UserTimecardService(
    UserTimecardRepository userTimecardRepository
    ) {
        this.userTimecardRepository = userTimecardRepository;
    }

    @Override
    public UserTimecard save(UserTimecard userTimecard) {
        logger.debug("Request to save UserTimecard {} ", userTimecard);
        UserTimecard result = null;
        try {
            result = userTimecardRepository.save(userTimecard);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete UserTimecard {} ", id);
        try {
            userTimecardRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<UserTimecard> findAll() {
        return userTimecardRepository.findAll();
    }

    @Override
    public Optional<UserTimecard> findOne(Long id) {
        return userTimecardRepository.findById(id);
    }
}
