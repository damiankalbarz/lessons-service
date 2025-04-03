package com.example.lessons_service.auth.config;


import com.example.lessons_service.auth.user.Permission;
import com.example.lessons_service.auth.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/membership-pass/**",
            "/api/v1/auth/google-authenticate",
            "/chat/**",
            "/api/v1/personal-trainers/**"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                //.requestMatchers(GET, "/api/v1/personal-trainer/**").permitAll()
                                //.requestMatchers(POST, "/api/v1/personal-trainer/**").permitAll()
                                // Endpointy dla zalogowanych użytowkników
                                /*.requestMatchers(GET, "/api/users/me", "/api/v1/personal-trainer/**", "/api/v1/classes/**").hasRole(Role.USER.name())
                                .requestMatchers(POST, "/api/v1/training-goals").hasRole(Role.USER.name())
                                .requestMatchers(PUT, "/api/v1/training-goals/**").hasRole(Role.USER.name())
                                .requestMatchers(DELETE, "/api/v1/training-goals/**").hasRole(Role.USER.name())
                                // Endpointy dla administratorów i menedżerów
                                .requestMatchers("/app/adminReply/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.MANAGER_READ.name())
                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(), Permission.MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(), Permission.MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(), Permission.MANAGER_DELETE.name())
                                //.requestMatchers(POST, "/api/v1/personal-trainer").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                //.requestMatchers(DELETE, "/api/v1/personal-trainer/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                //.requestMatchers(POST, "/api/v1/personal-trainer").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                //.requestMatchers(DELETE, "/api/v1/personal-trainer/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())*/
                                .anyRequest()
                                .authenticated()
                )
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));  // Allow requests from this origin
        configuration.setAllowedMethods(Arrays.asList("*"));  // Allow all methods
        configuration.setAllowedHeaders(Arrays.asList("*"));  // Allow all headers
        configuration.setAllowCredentials(true);  // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS configuration to all endpoints

        return source;
    }
}