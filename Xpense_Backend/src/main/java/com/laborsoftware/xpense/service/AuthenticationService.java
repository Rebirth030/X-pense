package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import com.laborsoftware.xpense.mapper.UserMapper;
import com.laborsoftware.xpense.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@Transactional
@RestController
@RequestMapping("/auth")
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public ApplicationUser signup(UserDTO input) {
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        ApplicationUser user = userMapper.toEntity(input);


        return userRepository.save(user);
    }

    public ApplicationUser login(UserDTO input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
        } catch (Exception e){
            e.printStackTrace();
        }

        return userRepository.findByUsername(input.getUsername()).orElseThrow();
    }

    @PostMapping("/signup")
    public ResponseEntity<ApplicationUser> register(@RequestBody UserDTO registerUserDto) {
        ApplicationUser registeredUser = this.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> authenticate(@RequestBody UserDTO loginUserDto) {
        ApplicationUser authenticatedUser = this.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        loginUserDto.setToken(jwtToken);
        loginUserDto.setTokenExpirationDate(authenticatedUser.getTokenExpirationDate());

        return ResponseEntity.ok(loginUserDto);
    }

}
