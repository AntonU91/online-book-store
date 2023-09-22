package org.example.config;

import lombok.AllArgsConstructor;
import org.example.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private UserDetailsService customUserDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                       .cors(AbstractHttpConfigurer::disable)
                       .csrf(AbstractHttpConfigurer::disable)
                       .authorizeHttpRequests(
                               auth -> auth.
                                               requestMatchers(
                                                       "/api/auth/**")
                                               .permitAll()
                                               .anyRequest()
                                               .authenticated())
//                       .headers(headers -> headers.frameOptions(
//                               HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                       .sessionManagement(
                               session -> session.sessionCreationPolicy(
                                       SessionCreationPolicy.STATELESS))
                       .addFilterBefore(jwtAuthenticationFilter,
                               UsernamePasswordAuthenticationFilter.class)
//                       .httpBasic(Customizer.withDefaults())
                       .userDetailsService(customUserDetailsService)
                       .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
