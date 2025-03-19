package com.example.online_auction_platform.securities;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public UserDetailsManager userDetailManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
            "select username, password, enabled from user where username=?"
        );

        // define query to retrieve role by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
            "select username, authority from authority where username=?"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    @Order(3) // Lower priority than Public and Admin configs
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> {
            // Auctioneer endpoints
            auth.requestMatchers(HttpMethod.GET, "/auctioneer/products").hasRole("AUCTIONEER");
            auth.requestMatchers(HttpMethod.PUT, "/auctioneer/auctioneer").hasRole("AUCTIONEER");
            auth.requestMatchers(HttpMethod.POST, "/auctioneer/products").hasRole("AUCTIONEER");
            auth.requestMatchers(HttpMethod.GET, "/auctioneer/products/**").hasRole("AUCTIONEER");
            auth.requestMatchers(HttpMethod.GET, "/auctioneer/productsByAuctioneerId/**").hasRole("AUCTIONEER");
            
            // Bidder endpoints
            auth.requestMatchers(HttpMethod.PUT, "/bidder/bidder").hasRole("BIDDER");
            auth.requestMatchers(HttpMethod.POST, "/bidder/bidden-price").hasRole("BIDDER");
            auth.requestMatchers(HttpMethod.GET, "/bidder/**").hasRole("BIDDER");
            
            // Product endpoints
            auth.requestMatchers(HttpMethod.GET, "/product/products").hasAnyRole("ADMIN", "AUCTIONEER", "BIDDER");
            auth.requestMatchers(HttpMethod.GET, "/product/products/**").hasAnyRole("ADMIN", "AUCTIONEER", "BIDDER");
            auth.requestMatchers(HttpMethod.POST, "/product/**").hasAnyRole("ADMIN", "AUCTIONEER");
            
            // OAuth endpoints
            auth.requestMatchers("/api/auth/**").permitAll();
            
            // Default deny all other requests
            auth.anyRequest().authenticated();
        });

        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.csrf(csrf -> csrf.disable());
        
        return httpSecurity.build();
    }
}
