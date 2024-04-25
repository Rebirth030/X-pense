package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.WeeklyTimecardMapper;
import com.laborsoftware.xpense.repository.WeeklyTimecardRepository;
import com.laborsoftware.xpense.service.crud.ICrudService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RestController
@RequestMapping
public class WeeklyTimecardService implements ICrudService<WeeklyTimecardDTO, Long> {

    Logger logger = LoggerFactory.getLogger(WeeklyTimecardService.class);

    private final WeeklyTimecardRepository weeklyTimecardRepository;

    @Autowired
    public final WeeklyTimecardMapper weeklyTimecardMapper;

    public WeeklyTimecardService(
            WeeklyTimecardRepository weeklyTimecardRepository,
            WeeklyTimecardMapper weeklyTimecardMapper
    ) {
        this.weeklyTimecardRepository = weeklyTimecardRepository;
        this.weeklyTimecardMapper = weeklyTimecardMapper;
    }


    @Override
    @PostMapping("/weekly-timecards")
    public ResponseEntity<WeeklyTimecardDTO> save(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO) {
        logger.debug("Request to save WeeklyTimecard {} ", weeklyTimecardDTO);
        try {
            WeeklyTimecard weeklyTimecard = weeklyTimecardMapper.toEntity(weeklyTimecardDTO);
            weeklyTimecard = weeklyTimecardRepository.save(weeklyTimecard);
            WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecard);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping("/weekly-timecards/{id}")
    public ResponseEntity<WeeklyTimecardDTO> update(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO, @PathVariable Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardRepository.findById(id);
            if (optionalWeeklyTimecard.isPresent()) {
                WeeklyTimecard weeklyTimecard = optionalWeeklyTimecard.get();
                weeklyTimecard = weeklyTimecardMapper.toEntity(weeklyTimecardDTO);
                weeklyTimecard = weeklyTimecardRepository.save(weeklyTimecard);
                WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecard);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/weekly-timecards/{id}")
    public void delete(Long id) {
        logger.debug("Request to delete WeeklyTimecard {} ", id);
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardRepository.findById(id);
            if (optionalWeeklyTimecard.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
            weeklyTimecardRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/weekly-timecards")
    public ResponseEntity<List<WeeklyTimecardDTO>> findAll() {
        List<WeeklyTimecard> weeklyTimecards = weeklyTimecardRepository.findAll();
        List<WeeklyTimecardDTO> result = weeklyTimecards.stream().map(weeklyTimecardMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @Override
    @GetMapping("/weekly-timecards/{id}")
    public ResponseEntity<WeeklyTimecardDTO> findOne(Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardRepository.findById(id);
            if (optionalWeeklyTimecard.isPresent()) {
                WeeklyTimecard weeklyTimecard = optionalWeeklyTimecard.get();
                WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecard);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
