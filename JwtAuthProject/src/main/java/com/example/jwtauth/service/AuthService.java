
package com.example.jwtauth.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jwtauth.dto.RegisterRequest;
import com.example.jwtauth.dto.LoginRequest;
import com.example.jwtauth.dto.AuthResponse;
import com.example.jwtauth.entity.User;
import com.example.jwtauth.repository.UserRepository;
import com.example.jwtauth.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // USER REGISTRATION
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());

        user.setPassword(
            passwordEncoder.encode(request.password())
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    // USER LOGIN
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() ->
                new RuntimeException("Invalid email or password")
            );

        if (!passwordEncoder.matches(
                request.password(), user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
            token,
            "Login successful"
        );
    }
}
