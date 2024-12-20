package com.example.onlineAuctionPlatform.securities;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        // define query to retrive a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
            "select username, password, enabled from user where username=?"
            );

        // define query to retrive role by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
            "select username, authority from authority where username=?"
            );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer -> configurer
            .requestMatchers(HttpMethod.GET, "/admin/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/admin/users").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/admin/authorities").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/auctioneer/products").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.PUT, "/auctioneer/auctioneer").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.POST, "/auctioneer/products").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.GET, "/auctioneer/products/**").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.GET, "/hello").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.GET, "/auctioneer/productsByAuctioneerId/**").hasRole("AUCTIONEER")
            .requestMatchers(HttpMethod.PUT, "/bidder/bidder").hasRole("BIDDER")
            .requestMatchers(HttpMethod.POST, "/bidder/bidden-price").hasRole("BIDDER")
            .requestMatchers(HttpMethod.GET, "/info").permitAll()
        );
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }
}
