package com.reactiverates.users.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.debug("Processing request: {} {}", method, requestURI);
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (StringUtils.hasText(token)) {
                if (jwtService.validateToken(token)) {
                    String username = jwtService.extractUsername(token);
                    List<String> roles = jwtService.extractRoles(token);
                    String tokenType = jwtService.extractTokenType(token);
                    
                    log.info("JWT token validation successful - Type: {}, User: {}, Roles: {}", 
                             tokenType, username, roles);
                    
                    // Создаем authorities с префиксом ROLE_ для Spring Security
                    List<SimpleGrantedAuthority> springAuthorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                    
                    log.info("Created Spring authorities: {} for user: {}", springAuthorities, username);
                    
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, springAuthorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JWT authentication successful for user: {} with authorities: {} for request: {} {}", 
                             username, springAuthorities, method, requestURI);
                } else {
                    log.warn("JWT token validation failed for request: {} {}", method, requestURI);
                    SecurityContextHolder.clearContext();
                }
            } else {
                log.debug("No JWT token found for request: {} {}", method, requestURI);
            }
        } catch (Exception e) {
            log.error("JWT authentication error for request {} {}: {}", method, requestURI, e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
