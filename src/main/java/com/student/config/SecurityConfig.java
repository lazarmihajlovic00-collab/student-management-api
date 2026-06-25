package com.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public  SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .httpBasic(httpBasic -> httpBasic.disable())

                .formLogin(form -> form.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/auth/**",
                                "/v1",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/students/*/graduate")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/students/*/suspend")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/students/*/activate")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/departments/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/departments/**"
                        )
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/students/*/department/*"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/students/*/courses/*")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
