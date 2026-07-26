package hexlet.code.config;

import hexlet.code.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration with JWT authentication.
 * Public endpoints use a separate filter chain so a stale/invalid Bearer token
 * cannot block login or static assets.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Public endpoints without JWT validation.
     * @param http http security
     * @return filter chain
     * @throws Exception on configuration errors
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        // Stateless JWT API does not use cookie sessions, so CSRF protection is not applicable.
        return http
                .securityMatchers(matchers -> matchers
                        .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico", "/api/login")
                        .requestMatchers(HttpMethod.POST, "/api/users")
                        .requestMatchers(HttpMethod.GET, "/welcome", "/sentry-debug")
                        .requestMatchers("/api-docs/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"))
                .csrf(csrf -> csrf.disable()) // NOSONAR java:S4502
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Protected API endpoints with JWT authentication.
     * @param http http security
     * @param jwtDecoder JWT decoder
     * @return filter chain
     * @throws Exception on configuration errors
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        // Stateless JWT API does not use cookie sessions, so CSRF protection is not applicable.
        return http
                .csrf(csrf -> csrf.disable()) // NOSONAR java:S4502
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .build();
    }

    /**
     * Provides authentication manager.
     * @param passwordEncoder password encoder
     * @param userService user details service
     * @return authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService userService
    ) {
        var provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
