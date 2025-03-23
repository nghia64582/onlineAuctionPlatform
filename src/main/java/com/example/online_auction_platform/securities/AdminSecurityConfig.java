package com.example.online_auction_platform.securities;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@Order(2) // Highest priority
public class AdminSecurityConfig {

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
    public SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();
            auth.anyRequest().hasAnyRole("ADMIN");
            // auth.requestMatchers(HttpMethod.GET, "/admin/users/**").hasRole("ADMIN");
            // auth.requestMatchers(HttpMethod.GET, "/admin/users").hasRole("ADMIN");
            // auth.requestMatchers(HttpMethod.POST, "/admin/authorities").hasRole("ADMIN");
            // auth.anyRequest().permitAll();
        });
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
