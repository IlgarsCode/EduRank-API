package com.ilgarscode.edurank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "mySuperSecretJwtKeyForEduRankProject2026";

    public String generateToken(
            String studentId,
            String role
    ) {

        return Jwts.builder()
                .setSubject(studentId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        )
                )
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        SECRET.getBytes()
                )
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractStudentId(
            String token
    ) {

        return extractClaims(token)
                .getSubject();
    }

    public String extractRole(
            String token
    ) {

        return extractClaims(token)
                .get("role", String.class);
    }
}