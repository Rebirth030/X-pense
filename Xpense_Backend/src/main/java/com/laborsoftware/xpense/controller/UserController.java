package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;
import com.laborsoftware.xpense.exceptions.DuplicateEmailException;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserMapper;
import com.laborsoftware.xpense.service.UserService;
import com.laborsoftware.xpense.service.UserTimecardService;
import liquibase.plugin.AbstractPlugin;
import org.mapstruct.control.MappingControl;
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
    ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            if (eMailIsPresent(userDTO.geteMail())) {
                throw new DuplicateEmailException("Email-Adresse wird bereits von einem anderen Kunden verwendet");
            }
            ApplicationUser applicationUser = userMapper.toEntity(userDTO);
            if(applicationUser.getRole() == null) {
                applicationUser.setRole(ApplicationUserRole.EMPLOYEE); // default
            }
            if(userDTO.getUserTimecardId() == null) {
                //applicationUser = userService.save(applicationUser);

                UserTimecard userTimecard = new UserTimecard(); // default initial user timecard
                userTimecard.setWeeklyWorkingHours(40.0);
                userTimecard.setBalance(0.0);
                userTimecard.setUserBalance(0.0);

                userTimecard = userTimecardService.save(userTimecard);
                applicationUser.setUserTimecard(userTimecard);

            }
            applicationUser = userService.save(applicationUser);
            UserDTO result = userMapper.toDto(applicationUser);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        try {
            Optional<ApplicationUser> optionalUser = userService.findOne(id);
            if (optionalUser.isPresent()) {
                ApplicationUser applicationUser = optionalUser.get();
                applicationUser = userMapper.toEntity(userDTO);
                applicationUser = userService.save(applicationUser);
                UserDTO result = userMapper.toDto(applicationUser);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<ApplicationUser> users = userService.findAll();
        List<UserDTO> result = users.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        try {
            Optional<ApplicationUser> optionalUser = userService.findOne(id);
            if (optionalUser.isPresent()) {
                ApplicationUser user = optionalUser.get();
                UserDTO result = userMapper.toDto(user);
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

    @DeleteMapping("/users/{id}")
    ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        try {
            ApplicationUser applicationUserToDelete = userService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            userService.delete(id);
            UserDTO result = userMapper.toDto(applicationUserToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private boolean eMailIsPresent(String email) {
        List<ApplicationUser> allApplicationUsers = userService.findAll();
        for (ApplicationUser applicationUser : allApplicationUsers) {
            if (applicationUser.geteMail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
