package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserTimecardMapper;
import com.laborsoftware.xpense.service.UserService;
import com.laborsoftware.xpense.service.UserTimecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserTimecardController {

    public final UserTimecardService userTimecardService;

    public final UserService userService;

    @Autowired
    public final UserTimecardMapper userTimecardMapper;

    public UserTimecardController(
            UserTimecardService userTimecardService,
            UserTimecardMapper userTimecardMapper,
            UserService userService
    ) {
        this.userTimecardService = userTimecardService;
        this.userTimecardMapper = userTimecardMapper;
        this.userService = userService;
    }


    /**
     * Updates the user timecard of User. User without UserTimecard should not exist.
     *
     * @param userTimecardDTO new UserTimecard
     * @param userId of owner of this UserTimecard
     *
     * @return User with updated UserTimecard
     */
    @PostMapping("/user-timecards/{userId}")
    ResponseEntity<User> createEvent(@RequestBody UserTimecardDTO userTimecardDTO, @PathVariable Long userId) {
        try {
            Optional<User> optionalUser = userService.findOne(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                user.setUserTimecard(userTimecard);
                //userTimecard.setUser(user);
                user = userService.save(user);
                return ResponseEntity.ok().body(user);
            } else {
                throw new ResourceNotFoundException("User timecard must have user. User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecard> updateEvent(@RequestBody UserTimecardDTO userTimecardDTO, @PathVariable Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne(id);
            if (optionalUserTimecard.isPresent()) {
                //Optional<User> optionalUser = userService.findUserByUserTimecardId(optionalUserTimecard.get().getId());
                //if(optionalUser.isPresent()) {
                    UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                    userTimecard = userTimecardService.save(userTimecard);
                    return ResponseEntity.ok().body(userTimecard);

                    /*
                    User user = optionalUser.get();
                    UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                    user.setUserTimecard(userTimecard);
                    userTimecard.setUser(user);
                    userTimecard.setId(id);
                    userTimecardService.save(userTimecard);

                    user = userService.save(user);
                    return ResponseEntity.ok().body(user);

                } else {
                    throw new ResourceNotFoundException("User timecard must have user. User not found");
                }*/
            } else {
                throw new ResourceNotFoundException("User timecard not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user-timecards")
    ResponseEntity<List<UserTimecard>> getAllEvents() {
        return new ResponseEntity<>(userTimecardService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecard> getEvent(@PathVariable Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne(id);
            if (optionalUserTimecard.isPresent()) {
                optionalUserTimecard.get();
                return ResponseEntity.ok().body(optionalUserTimecard.get());
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

    @DeleteMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecard> deleteEvent(@PathVariable Long id) {
        try {
            UserTimecard userTimecardToDelete = userTimecardService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            userTimecardService.delete(id);
            return new ResponseEntity<>(userTimecardToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
