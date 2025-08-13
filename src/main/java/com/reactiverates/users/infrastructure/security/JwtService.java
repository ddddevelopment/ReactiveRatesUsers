package com.reactiverates.users.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            List<String> roles = claims.get("roles", List.class);
            
            if (roles != null) {
                return roles;
            }
            
            return List.of();
        } catch (Exception e) {
            log.error("Error extracting roles from JWT token: {}", e.getMessage());
            return List.of();
        }
    }

    public String extractTokenType(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("type", String.class);
        } catch (Exception e) {
            log.error("Error extracting token type from JWT token: {}", e.getMessage());
            return null;
        }
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            // Проверяем тип токена
            String tokenType = claims.get("type", String.class);
            if (tokenType == null || !"access".equals(tokenType)) {
                log.error("Invalid token type: {}", tokenType);
                return false;
            }
            
            // Проверяем время создания
            Date issuedAt = claims.getIssuedAt();
            if (issuedAt == null || issuedAt.after(new Date())) {
                log.error("Invalid token issued at time: {}", issuedAt);
                return false;
            }
            
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
