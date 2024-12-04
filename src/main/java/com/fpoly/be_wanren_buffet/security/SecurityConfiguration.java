package com.fpoly.be_wanren_buffet.security;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;



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
                        // Public GET Endpoints
                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_PORT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, Endpoints.PRIVATE_PUT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, Endpoints.PUBLIC_PUT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/customer/updatePhoneNumber/**").hasAuthority("CUSTOMER")
                        // Các endpoint khác được phép truy cập mà không cần xác thực
                        .requestMatchers("/login**", "/oauth2/**", "/api/product/**", "/assets/**").permitAll()
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
                        // Private Endpoints for Staff
                        .requestMatchers(HttpMethod.GET, Endpoints.PRIVATE_GET_STAFF).hasAnyAuthority("STAFF","ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoints.PRIVATE_POST_STAFF).hasAnyAuthority("STAFF","ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoints.PRIVATE_PUT_STAFF).hasAnyAuthority("STAFF","ADMIN")
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .authenticationProvider(userAuthenticationProvider()) // Add User provider
                .authenticationProvider(customerAuthenticationProvider()) // Add Customer provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT Filter
                // OAuth2 Login Configuration for Google
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login") // Specify custom login page if needed
                        .successHandler(oauth2AuthenticationSuccessHandler) // Use the custom success handler
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://wanrenbuffet.netlify.app")); // Change according to your frontend URL
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
