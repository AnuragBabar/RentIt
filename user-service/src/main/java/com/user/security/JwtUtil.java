package com.user.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private static final String SECRET ="myjwtsecretkeymyjwtsecretkeymyjwtsecretkey123";
	private static final SecretKey key =Keys.hmacShaKeyFor(SECRET.getBytes());
	
	private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(Long userId, String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
