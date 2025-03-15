package com.example.online_auction_platform.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1) // Highest priority
public class PublicSecurityConfig {

    @Bean
    public SecurityFilterChain publicSecurity(HttpSecurity http) throws Exception {
        http.securityMatcher("/public/**", "/", "/login/**", "/register/**", "/avatars/**", "/api/auth/**")
            .authorizeHttpRequests(auth -> {
                // Static resources and public pages
                auth.requestMatchers("/avatars/**").permitAll();
                auth.requestMatchers("/").permitAll();
                auth.requestMatchers("/login/**").permitAll();
                auth.requestMatchers("/register/**").permitAll();
                auth.requestMatchers("/public/**").permitAll();
                
                // OAuth endpoints
                auth.requestMatchers("/api/auth/**").permitAll();
                
                // Product viewing endpoints (public access)
                auth.requestMatchers(HttpMethod.GET, "/product/view/**").permitAll();
                
                // Swagger/OpenAPI endpoints if you have them
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
            });
            
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
