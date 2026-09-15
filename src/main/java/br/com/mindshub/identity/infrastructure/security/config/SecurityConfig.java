package br.com.mindshub.identity.infrastructure.security.config;

import br.com.mindshub.identity.infrastructure.security.jwt.JwtAuthenticationFilter;
import br.com.mindshub.shared.presentation.exception.CustomAccessDeniedHandler;
import br.com.mindshub.shared.presentation.exception.CustomAuthorityEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthorityEntryPoint customAuthorityEntryPoint) throws Exception {

        http.
                csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthorityEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // PUBLIC
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/verify-email",
                                "/api/v1/auth/resend-verification"
                        ).permitAll()

                        .requestMatchers(
                                // TEACHER
                                "/api/v1/teachers/create",
                                "/api/v1/teachers/promote/{uuid}/teacher"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                // USERS
                                "/api/v1/users",
                                "/api/v1/users/by-role",
                                "/api/v1/users/status",
                                "/api/v1/users/{uuid}",
                                "/api/v1/users/{uuid}/delete"
                        ).hasRole("ADMIN")

                        .requestMatchers("/api/v1/users/me").authenticated()

                        .requestMatchers(
                                // ADMIN
                                "/api/v1/admin/create",
                                "/api/v1/admin/promote/{uuid}/admin"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/users/me/update",
                                "/api/v1/users/me/update/password"
                        ).authenticated()
                        .anyRequest().authenticated()

                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
