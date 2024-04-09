package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.UserTimecard;
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
import java.util.Set;

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
    ResponseEntity<WeeklyTimecard> createEvent(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO) {
        try {
            WeeklyTimecard weeklyTimecard = weeklyTimecardMapper.toEntity(weeklyTimecardDTO);
            weeklyTimecard = weeklyTimecardService.save(weeklyTimecard);

            /*
            Optional<User> optionalUser = userService.findOne(weeklyTimecard.getUser().getId());
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                Set<WeeklyTimecard> userWeeklyTimecards = user.getWeeklyTimecards();
                userWeeklyTimecards.add(weeklyTimecard);
                user.setWeeklyTimecards(userWeeklyTimecards);
                userService.save(user);
            }


            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne(weeklyTimecard.getUserTimecard().getId());
            if(optionalUserTimecard.isPresent()) {
                UserTimecard userTimecard = optionalUserTimecard.get();
                Set<WeeklyTimecard> userTimecardWeeklyTimecards = userTimecard.getWeeklyTimecards();
                userTimecardWeeklyTimecards.add(weeklyTimecard);
                userTimecard.setWeeklyTimecards(userTimecardWeeklyTimecards);
                userTimecardService.save(userTimecard);
            }
            */
            return ResponseEntity.ok().body(weeklyTimecard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/weekly-timecards/{id}")
    ResponseEntity<WeeklyTimecard> updateEvent(@RequestBody WeeklyTimecardDTO weeklyTimecardDTO, @PathVariable Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardService.findOne(id);
            if(optionalWeeklyTimecard.isPresent()) {
                return ResponseEntity.ok().body(weeklyTimecardService.save(weeklyTimecardMapper.toEntity(weeklyTimecardDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/weekly-timecards")
    ResponseEntity<List<WeeklyTimecard>> getAllEvents() {
        return new ResponseEntity<>(weeklyTimecardService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/weekly-timecards/{id}")
    ResponseEntity<WeeklyTimecard> getEvent(@PathVariable Long id) {
        try {
            Optional<WeeklyTimecard> optionalWeeklyTimecard = weeklyTimecardService.findOne(id);
            if(optionalWeeklyTimecard.isPresent()) {
                optionalWeeklyTimecard.get();
                return ResponseEntity.ok().body(optionalWeeklyTimecard.get());
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
    ResponseEntity<WeeklyTimecard> deleteEvent(@PathVariable Long id) {
        try {
            WeeklyTimecard weeklyTimecardToDelete = weeklyTimecardService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            weeklyTimecardService.delete(id);
            return new ResponseEntity<>(weeklyTimecardToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
