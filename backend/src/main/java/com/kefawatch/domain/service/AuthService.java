package com.kefawatch.domain.service;

import com.kefawatch.domain.exception.DomainExceptions;
import com.kefawatch.domain.model.UserAccount;
import com.kefawatch.domain.port.UserRepository;
import com.kefawatch.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw DomainExceptions.conflict("Username already taken");
        }
        String hash = passwordEncoder.encode(rawPassword);
        long id = userRepository.insert(username, hash);
        return jwtService.generateToken(id, username);
    }

    public String login(String username, String rawPassword) {
        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, user.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return jwtService.generateToken(user.id(), user.username());
    }
}
