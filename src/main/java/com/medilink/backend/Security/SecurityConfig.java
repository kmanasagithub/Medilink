package com.medilink.backend.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        //PUBLIC END POINTS
                        .requestMatchers("/auth/**")
                        .permitAll()

                        //ADMIN-ONLY
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        //HOSPITAL-ONLY
                        .requestMatchers("/hospitals/**")
                        .hasRole("HOSPITAL")

                        //DOCTOR-ONLY
                        .requestMatchers("/doctors/**")
                        .hasRole("DOCTOR")

                        //PATIENT-ONLY
                        .requestMatchers("/patients/**")
                        .hasRole("PATIENT")

                        // Everything else requires authentication
                        .anyRequest()
                        .authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}
