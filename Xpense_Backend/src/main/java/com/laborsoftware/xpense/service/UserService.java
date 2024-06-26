package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.UserMapper;
import com.laborsoftware.xpense.repository.UserRepository;
import com.laborsoftware.xpense.service.crud.ICrudService;
import jakarta.transaction.Transactional;
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
public class UserService implements ICrudService<UserDTO, Long> {
    private final UserRepository userRepository;

    @Autowired
    public final UserMapper userMapper;


    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    @PostMapping("/users")
    public ResponseEntity<UserDTO> save(@RequestHeader("Authorization") String token,@RequestBody UserDTO userDTO) {
        logger.debug("Request to save User {} ", userDTO);
        try {

            ApplicationUser applicationUser = userMapper.toEntity(userDTO);
            if(applicationUser.getRole() == null) {
                applicationUser.setRole(ApplicationUserRole.EMPLOYEE);
            }

            applicationUser = userRepository.save(applicationUser);
            UserDTO result = userMapper.toDto(applicationUser);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> update(@RequestHeader("Authorization") String token,@RequestBody UserDTO userDTO, @PathVariable Long id) {
        try {
            Optional<ApplicationUser> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                ApplicationUser applicationUser = optionalUser.get();
                applicationUser = userMapper.toEntity(userDTO);
                applicationUser = userRepository.save(applicationUser);
                UserDTO result = userMapper.toDto(applicationUser);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/users/{id}")
    public void delete(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        logger.debug("Request to delete User {} ", id);
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll(@RequestHeader("Authorization") String token) {
        List<ApplicationUser> users = userRepository.findAll();
        List<UserDTO> result = users.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @Override
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> findOne(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        try {
            Optional<ApplicationUser> optionalApplicationUser = userRepository.findById(id);
            if(optionalApplicationUser.isPresent()) {
                ApplicationUser user = optionalApplicationUser.get();
                UserDTO result = userMapper.toDto(user);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/users/getByToken")
    public ResponseEntity<UserDTO> getByToken(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.split(" ")[1];
            ApplicationUser user = userRepository.findByToken(tokenValue).orElseThrow();
            UserDTO result = userMapper.toDto(user);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
