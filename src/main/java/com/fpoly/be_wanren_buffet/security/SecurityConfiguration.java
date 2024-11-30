package com.fpoly.be_wanren_buffet.security;

import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import com.fpoly.be_wanren_buffet.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private CustomerAuthService customerAuthService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider for User
    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userAuthService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Authentication Provider for Customer
    @Bean
    public DaoAuthenticationProvider customerAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerAuthService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Enable CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(authz -> authz
                        // Public Endpoints
                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_PORT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, Endpoints.PUBLIC_PUT_ENDPOINTS).permitAll()
                        // Private Endpoints for Cashiers
                        .requestMatchers(HttpMethod.POST, Endpoints.PRIVATE_POST_CASHIER).permitAll()
                        .requestMatchers(HttpMethod.PATCH, Endpoints.PRIVATE_PATCH_CASHIER).permitAll()
                        .requestMatchers(HttpMethod.PUT, Endpoints.PRIVATE_PUT_CASHIER).permitAll()
                        // Private Endpoints for Customers
                        .requestMatchers(HttpMethod.GET, Endpoints.PRIVATE_CUSTOMER_GET_ENDPOINTS).hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, Endpoints.PRIVATE_POST_ENDPOINTS).hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, Endpoints.PRIVATE_PUT_ENDPOINTS).hasAuthority("CUSTOMER")
                        // Private Endpoints for Admins
                        .requestMatchers(HttpMethod.GET, Endpoints.PRIVATE_GET_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoints.PRIVATE_POST_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.PRIVATE_PUT_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, Endpoints.PRIVATE_PATCH_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Endpoints.PRIVATE_DELETE_ADMIN).hasAuthority("ADMIN")
                        // All other endpoints require authentication

                        // Các dòng cho User bị comment
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .authenticationProvider(userAuthenticationProvider()) // Add User provider
                .authenticationProvider(customerAuthenticationProvider()) // Add Customer provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT Filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Change according to your frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Allowed HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
        configuration.setMaxAge(3600L); // Cache CORS preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS to all endpoints
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(userAuthenticationProvider(), customerAuthenticationProvider()));
    }
}
