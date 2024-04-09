package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import com.laborsoftware.xpense.exceptions.DuplicateEmailException;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserMapper;
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
public class UserController {

    public final UserService userService;

    private final UserTimecardService userTimecardService;

    @Autowired
    public final UserMapper userMapper;

    public UserController(
            UserService userService,
            UserMapper userMapper,
            UserTimecardService userTimecardService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userTimecardService = userTimecardService;
    }


    @PostMapping("/users")
    ResponseEntity<User> createEvent(@RequestBody UserDTO userDTO) {
        try {
            if (eMailIsPresent(userDTO.geteMail())) {
                throw new DuplicateEmailException("Email-Adresse wird bereits von einem anderen Kunden verwendet");
            }
            User user = userMapper.toEntity(userDTO);
            if(user.getUserTimecard() == null) {
                user = userService.save(user);

                /**
                 * TODO:
                 * Create UserTimecard according the role of user (Freelancer/Werkstudent/Employess/...)
                 * For example Werkstudent can max work 20 h per week
                 */
                UserTimecard userTimecard = new UserTimecard();
                userTimecard.setWeeklyWorkingHours(0.0);
                userTimecard.setBalance(0.0);
                userTimecard.setUserBalance(0.0);
                // userTimecard.setUser(user);
                userTimecard = userTimecardService.save(userTimecard);
                user.setUserTimecard(userTimecard);
            }
            user = userService.save(user);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<User> updateEvent(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        try {
            Optional<User> optionalUser = userService.findOne(id);
            if (optionalUser.isPresent()) {
                return ResponseEntity.ok().body(userService.save(userMapper.toEntity(userDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users")
    ResponseEntity<List<User>> getAllEvents() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<User> getEvent(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userService.findOne(id);
            if (optionalUser.isPresent()) {
                optionalUser.get();
                return ResponseEntity.ok().body(optionalUser.get());
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

    @DeleteMapping("/users/{id}")
    ResponseEntity<User> deleteEvent(@PathVariable Long id) {
        try {
            User userToDelete = userService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            userService.delete(id);
            return new ResponseEntity<>(userToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private boolean eMailIsPresent(String email) {
        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            if (user.geteMail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
