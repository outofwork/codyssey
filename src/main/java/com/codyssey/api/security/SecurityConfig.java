package com.codyssey.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/v1/users/register").permitAll()
                        .requestMatchers("/v1/users/check-**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        
                        // Guest access - allow all GET requests without authentication
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/articles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/coding-questions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/labels/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/sources/**").permitAll()
                        
                        // Modification operations - authentication required
                        .requestMatchers(HttpMethod.POST, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/v1/articles/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/v1/coding-questions/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/coding-questions/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/coding-questions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/v1/coding-questions/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/v1/labels/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/labels/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/labels/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/v1/labels/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/v1/sources/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/sources/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/v1/sources/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/v1/sources/**").authenticated()
                        
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
