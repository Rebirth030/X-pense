package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserTimecardMapper;
import com.laborsoftware.xpense.service.UserService;
import com.laborsoftware.xpense.service.UserTimecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param userId          of owner of this UserTimecard
     * @return User with updated UserTimecard
     */
    @PostMapping("/user-timecards")
    ResponseEntity<UserTimecardDTO> createUserTimecard(@RequestBody UserTimecardDTO userTimecardDTO, @PathVariable Long userId) {
        try {
            UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
            userTimecard = userTimecardService.save(userTimecard);
            UserTimecardDTO result = userTimecardMapper.toDto(userTimecard);
            return ResponseEntity.ok().body(result);

            /*
            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne()

            Optional<ApplicationUser> optionalUser = userService.findOne(userId);
            if (optionalUser.isPresent()) {
                ApplicationUser applicationUser = optionalUser.get();
                UserTimecard userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                applicationUser.setUserTimecard(userTimecard);
                //userTimecard.setUser(user);
                applicationUser = userService.save(applicationUser);
                return ResponseEntity.ok().body(applicationUser);
            } else {
                throw new ResourceNotFoundException("User timecard must have user. User not found");
            }

             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecardDTO> updateUserTimecard(@RequestBody UserTimecardDTO userTimecardDTO, @PathVariable Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne(id);
            if (optionalUserTimecard.isPresent()) {
                UserTimecard userTimecard = optionalUserTimecard.get();
                userTimecard = userTimecardMapper.toEntity(userTimecardDTO);
                userTimecard = userTimecardService.save(userTimecard);
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

    @GetMapping("/user-timecards")
    ResponseEntity<List<UserTimecardDTO>> getAllUserTimecards() {
        List<UserTimecard> userTimecards =  userTimecardService.findAll();
        List<UserTimecardDTO> result = userTimecards.stream().map(userTimecardMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecardDTO> getUserTimecard(@PathVariable Long id) {
        try {
            Optional<UserTimecard> optionalUserTimecard = userTimecardService.findOne(id);
            if (optionalUserTimecard.isPresent()) {
                UserTimecard userTimecard = optionalUserTimecard.get();
                UserTimecardDTO result = userTimecardMapper.toDto(userTimecard);
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

    @DeleteMapping("/user-timecards/{id}")
    ResponseEntity<UserTimecardDTO> deleteUserTimecard(@PathVariable Long id) {
        try {
            UserTimecard userTimecardToDelete = userTimecardService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            userTimecardService.delete(id);
            UserTimecardDTO result = userTimecardMapper.toDto(userTimecardToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
