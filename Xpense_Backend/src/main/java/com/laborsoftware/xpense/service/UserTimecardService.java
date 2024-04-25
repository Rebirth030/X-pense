package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserMapper;
import com.laborsoftware.xpense.mapper.UserTimecardMapper;
import com.laborsoftware.xpense.repository.UserTimecardRepository;
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


@RestController
@RequestMapping
@Service
@Transactional
public class UserTimecardService implements ICrudService<UserTimecardDTO, Long> {

    Logger logger = LoggerFactory.getLogger(UserTimecardService.class);

    private final UserTimecardRepository userTimecardRepository;

    @Autowired
    public final UserTimecardMapper userTimecardMapper;

    public UserTimecardService(
            UserTimecardRepository userTimecardRepository,
            UserTimecardMapper userTimecardMapper
    ) {
        this.userTimecardRepository = userTimecardRepository;
        this.userTimecardMapper = userTimecardMapper;
    }

    @Override
    @PostMapping("/user-timecards")
    public ResponseEntity<UserTimecardDTO> save(@RequestBody UserTimecardDTO userTimecardDTO) {
        logger.debug("Request to save UserTimecard {} ", userTimecardDTO);
        try {
            UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
            userTimecard = userTimecardRepository.save(userTimecard);
            UserTimecardDTO result = userTimecardMapper.toDto(userTimecard);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping("/user-timecards/{id}")
    public ResponseEntity<UserTimecardDTO> update(@RequestBody UserTimecardDTO userTimecardDTO, @PathVariable Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardRepository.findById(id);
            if (optionalUserTimecard.isPresent()) {
                UserTimecard userTimecard = optionalUserTimecard.get();
                userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                userTimecard = userTimecardRepository.save(userTimecard);
                UserTimecardDTO result = userTimecardMapper.toDto(userTimecard);
                return ResponseEntity.ok().body(result);

            } else {
                throw new ResourceNotFoundException("User timecard not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/user-timecards/{id}")
    public void delete(Long id) {
        logger.debug("Request to delete UserTimecard {} ", id);
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardRepository.findById(id);
            if (optionalUserTimecard.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
            userTimecardRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/user-timecards")
    public ResponseEntity<List<UserTimecardDTO>> findAll() {
        List<UserTimecard> userTimecards = userTimecardRepository.findAll();
        List<UserTimecardDTO> result = userTimecards.stream().map(userTimecardMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @Override
    @GetMapping("/user-timecards/{id}")
    public ResponseEntity<UserTimecardDTO> findOne(Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardRepository.findById(id);
            if (optionalUserTimecard.isPresent()) {
                UserTimecard userTimecard = optionalUserTimecard.get();
                UserTimecardDTO result = userTimecardMapper.toDto(userTimecard);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
