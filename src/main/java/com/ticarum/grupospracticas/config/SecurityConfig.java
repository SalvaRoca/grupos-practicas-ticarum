package com.ticarum.grupospracticas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                    .requestMatchers(GET, "/asignaturas").hasAnyRole("PROFESOR", "ALUMNO")
                    .requestMatchers(POST, "/asignaturas").hasRole("PROFESOR")
                    .requestMatchers(PUT, "/asignaturas").hasRole("PROFESOR")
                    .requestMatchers(DELETE, "/asignaturas").hasRole("PROFESOR")
                    .requestMatchers("/grupos").hasAnyRole("PROFESOR", "ALUMNO")
                    .requestMatchers("/alumnos").hasAnyRole("PROFESOR", "ALUMNO")
                    .requestMatchers("/h2-console/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("alumno")
                        .password(passwordEncoder().encode("alumno1234"))
                        .roles("ALUMNO")
                        .build(),
                User.withUsername("profesor")
                        .password(passwordEncoder().encode("profesor1234"))
                        .roles("PROFESOR")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}