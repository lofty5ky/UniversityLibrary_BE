package com.example.librarymanagement.config;

import com.example.librarymanagement.model.Role;
import com.example.librarymanagement.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT Token
    public String generateToken(User user) {
        try {
            return Jwts.builder()
                    .issuer("tinng.dev")
                    .claim("email", user.getEmail())
                    .claim("userId", user.getUserId())
                    .claim("userName", user.getName())
                    .claim("roles", user.getRoles().stream()
                            .map(Role::getRoleName) // Lấy tên role
                            .collect(Collectors.toList())) // Lưu danh sách role vào claim
                    .issuedAt(new Date())
                    .expiration(new Date(
                            Instant.now().plus(expirationTime, ChronoUnit.SECONDS).toEpochMilli()
                    ))
                    .signWith(getSignKey())
                    .compact();
        } catch (Exception e) {
            log.error("Cannot create jwt, error: {}", e.getMessage());
            return null;
        }
    }

    // Lấy email từ JWT Token
    public String getEmailFromToken(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    // Lấy username từ JWT Token
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra tính hợp lệ của token
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // Thử giải mã token
            return true; // Token hợp lệ
        } catch (Exception e) {
            return false; // Token không hợp lệ
        }
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    // Lấy claim từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Giải mã token để lấy tất cả các claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
