
package com.example.jwtauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jwtauth.dto.RegisterRequest;
import com.example.jwtauth.dto.LoginRequest;
import com.example.jwtauth.dto.AuthResponse;
import com.example.jwtauth.service.AuthService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "JWT Authentication", description = "Register and Login APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 1. USER REGISTRATION API
    @PostMapping("/register")
    @Operation(summary = "Register New User")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        String response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    // 2. USER LOGIN API
    @PostMapping("/login")
    @Operation(summary = "Login User and Generate JWT Token")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    // 3. JWT PROTECTED API
    @GetMapping("/welcome")
    @Operation(
        summary = "Access Protected API",
        security = @SecurityRequirement(
            name = "Bearer Authentication"
        )
    )
    public ResponseEntity<String> welcome() {

        return ResponseEntity.ok(
            "JWT Authentication Successful! Welcome."
        );
    }
}
