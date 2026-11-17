
package com.example.jwtauth.dto;

public record AuthResponse(
    String token,
    String message
) {}
