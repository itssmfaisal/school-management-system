package com.school.management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.expiration-ms}") long expirationMs) {
        try {
            byte[] keyBytes = null;

            // If secret is Base64 encoded and yields a sufficiently long key, use it
            try {
                byte[] decoded = Base64.getDecoder().decode(secret);
                if (decoded.length >= 32) {
                    keyBytes = decoded;
                }
            } catch (IllegalArgumentException ignored) {
                // not base64, fall through
            }

            // If not base64 or too short, use raw bytes if long enough
            if (keyBytes == null) {
                byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
                if (raw.length >= 32) {
                    keyBytes = raw;
                } else {
                    // Derive a 256-bit key from the provided secret using SHA-256
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    keyBytes = md.digest(raw);
                }
            }

            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to initialize JWT key", ex);
        }

        this.expirationMs = expirationMs;
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
