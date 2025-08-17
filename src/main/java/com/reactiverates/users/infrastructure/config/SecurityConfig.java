package com.reactiverates.users.infrastructure.config;

import com.reactiverates.users.infrastructure.security.JwtAuthenticationEntryPoint;
import com.reactiverates.users.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                .requestMatchers("/v3/api-docs/**", "/api-docs/**", "/api-docs.yaml").permitAll()
                .requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
                .requestMatchers("/swagger-config", "/api-docs/swagger-config").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
               
                .requestMatchers("GET", "/api/users").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/{id}").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/username/{username}").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/email/{email}").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/role/{role}").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/active").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers("GET", "/api/users/search").hasAnyRole("USER", "MODERATOR", "ADMIN")
                
                .requestMatchers("POST", "/api/users").hasAnyRole("MODERATOR", "ADMIN")
                
                .requestMatchers("PATCH", "/api/users/{id}/activate").hasAnyRole("MODERATOR", "ADMIN")
                .requestMatchers("PATCH", "/api/users/{id}/deactivate").hasAnyRole("MODERATOR", "ADMIN")
                
                .requestMatchers("PUT", "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers("DELETE", "/api/users/{id}").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
