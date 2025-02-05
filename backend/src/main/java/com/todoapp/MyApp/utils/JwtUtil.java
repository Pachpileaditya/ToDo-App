package com.todoapp.MyApp.utils;

import com.todoapp.MyApp.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "4rQQ0XF7raAQn39Coyiv0jzgWLwkL7uN1v8BCjq6H9c2RjCGD0PjZjzmoT9vaVvD";
    private final long EXPIRATION_TIME = 86400000;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", List.of(user.getRole())) // Store role as a list
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream().map(Object::toString).collect(Collectors.toList());
        }
        return List.of("ROLE_USER"); // Default to ROLE_USER if null
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
