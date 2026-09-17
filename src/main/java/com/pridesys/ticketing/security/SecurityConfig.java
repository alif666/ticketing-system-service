package com.pridesys.ticketing.security;

import java.util.Map;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pridesys.ticketing.repository.UserRepository;
import com.pridesys.ticketing.security.util.JwtService;
import com.pridesys.ticketing.security.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwt,
            UserRepository users,
            @Qualifier("publicPaths") List<String> publicPaths,
            @Qualifier("appAdminPaths") List<String> appAdminPaths,
            @Qualifier("clientAdminPaths") List<String> clientAdminPaths,
            @Qualifier("clientAdminModuleWritePaths") List<String> clientAdminModuleWritePaths,
            @Qualifier("securedPaths") List<String> securedPaths) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return http.csrf(csrf -> csrf.disable()).cors(cors -> {
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    publicPaths.forEach(path -> auth.requestMatchers(path).permitAll());
                    appAdminPaths.forEach(path -> auth.requestMatchers(path).hasRole("APP_ADMIN"));
                    clientAdminPaths.forEach(path -> auth.requestMatchers(path)
                            .hasAnyRole("APP_ADMIN", "CLIENT_ADMIN"));
                    clientAdminModuleWritePaths.forEach(path -> {
                        auth.requestMatchers(HttpMethod.POST, path)
                                .hasAnyRole("APP_ADMIN", "CLIENT_ADMIN");
                        auth.requestMatchers(HttpMethod.PATCH, path)
                                .hasAnyRole("APP_ADMIN", "CLIENT_ADMIN");
                        auth.requestMatchers(HttpMethod.DELETE, path)
                                .hasAnyRole("APP_ADMIN", "CLIENT_ADMIN");
                    });
                    securedPaths.forEach(path -> auth.requestMatchers(path).authenticated());
                    auth.anyRequest().denyAll();
                })
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    mapper.writeValue(res.getOutputStream(), Map.of("error", "UNAUTHORIZED", "message", "Authentication is required"));
                }))
                .addFilterBefore(new JwtAuthenticationFilter(jwt, users), UsernamePasswordAuthenticationFilter.class).build();
    }
}
