package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.WeeklyTimecardMapper;
import com.laborsoftware.xpense.service.UserService;
import com.laborsoftware.xpense.service.UserTimecardService;
import com.laborsoftware.xpense.service.WeeklyTimecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class WeeklyTimecardController {


    public final WeeklyTimecardService weeklyTimecardService;

    public final UserService userService;

    public final UserTimecardService userTimecardService;

    @Autowired
    public final WeeklyTimecardMapper weeklyTimecardMapper;

    public WeeklyTimecardController(
            WeeklyTimecardService weeklyTimecardService,
            WeeklyTimecardMapper weeklyTimecardMapper,
            UserService userService,
            UserTimecardService userTimecardService
    ) {
        this.weeklyTimecardService = weeklyTimecardService;
        this.weeklyTimecardMapper = weeklyTimecardMapper;
        this.userService = userService;
        this.userTimecardService = userTimecardService;
    }


    @PostMapping("/weekly-timecards")
    ResponseEntity<WeeklyTimecardDTO> createWeeklyTimecard(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO) {
        try {
            WeeklyTimecard weeklyTimecard = weeklyTimecardMapper.toEntity(weeklyTimecardDTO);
            weeklyTimecard = weeklyTimecardService.save(weeklyTimecard);
            WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecard);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/weekly-timecards/{id}")
    ResponseEntity<WeeklyTimecardDTO> updateWeeklyTimecard(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO, @PathVariable Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardService.findOne(id);
            if(optionalWeeklyTimecard.isPresent()) {
                WeeklyTimecard weeklyTimecard = optionalWeeklyTimecard.get();
                weeklyTimecard =  weeklyTimecardMapper.toEntity(weeklyTimecardDTO);
                weeklyTimecard = weeklyTimecardService.save(weeklyTimecard);
                WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecard);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/weekly-timecards")
    ResponseEntity<List<WeeklyTimecardDTO>> getAllWeeklyTimecards() {
        List<WeeklyTimecard> weeklyTimecards = weeklyTimecardService.findAll();
        List<WeeklyTimecardDTO> result = weeklyTimecards.stream().map(weeklyTimecardMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/weekly-timecards/{id}")
    ResponseEntity<WeeklyTimecardDTO> getWeeklyTimecard(@PathVariable Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardService.findOne(id);
            if(optionalWeeklyTimecard.isPresent()) {
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

    @DeleteMapping("/weekly-timecards/{id}")
    ResponseEntity<WeeklyTimecardDTO> deleteWeeklyTimecard(@PathVariable Long id) {
        try {
            WeeklyTimecard weeklyTimecardToDelete = weeklyTimecardService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            weeklyTimecardService.delete(id);
            WeeklyTimecardDTO result = weeklyTimecardMapper.toDto(weeklyTimecardToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
