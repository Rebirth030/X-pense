package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.interfaces.IWeeklyTimecardService;
import com.laborsoftware.xpense.repository.WeeklyTimecardRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WeeklyTimecardService implements IWeeklyTimecardService {

    Logger logger = LoggerFactory.getLogger(WeeklyTimecardService.class);

    private final WeeklyTimecardRepository weeklyTimecardRepository;

    public WeeklyTimecardService(
            WeeklyTimecardRepository weeklyTimecardRepository
    ) {
        this.weeklyTimecardRepository = weeklyTimecardRepository;
    }


    @Override
    public WeeklyTimecard save(WeeklyTimecard weeklyTimecard) {
        logger.debug("Request to save WeeklyTimecard {} ", weeklyTimecard);
        WeeklyTimecard result = null;
        try {
            result = weeklyTimecardRepository.save(weeklyTimecard);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete WeeklyTimecard {} ", id);
        try {
            weeklyTimecardRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<WeeklyTimecard> findAll() {
        return weeklyTimecardRepository.findAll();
    }

    @Override
    public Optional<WeeklyTimecard> findOne(Long id) {
        return weeklyTimecardRepository.findById(id);
    }
}
