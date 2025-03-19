package com.lcwd.bridgelabz.addressbook.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // Generate Token
    public String generateToken(String username) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not set! Check application.properties.");
        }

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Validate Token
    public boolean validateToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                    .withSubject(username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Extract Username
    public String extractUsername(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    // Check if Token is Expired
    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            logger.error("Failed to check token expiration: {}", e.getMessage());
            return true; // Assume expired if decoding fails
        }
    }
}
