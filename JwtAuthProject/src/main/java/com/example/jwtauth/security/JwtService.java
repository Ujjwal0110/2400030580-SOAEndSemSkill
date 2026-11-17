
package com.example.jwtauth.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration) {

        this.secretKey = Keys.hmacShaKeyFor(
            secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String generateToken(String email) {

        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + expiration)
            )
            .signWith(secretKey)
            .compact();
    }

    public boolean validateJwtToken(String token) {

        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
