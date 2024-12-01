package com.example.EventosP1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()

                        // USUARIOS
                        .requestMatchers("/usuarios/all").hasAuthority("SUPERADMIN")
                        .requestMatchers("/usuarios/save").hasAuthority("SUPERADMIN")
                        .requestMatchers("/usuarios/update").hasAuthority("SUPERADMIN")
                        .requestMatchers("/usuarios/change-status").hasAuthority("SUPERADMIN")
                        .requestMatchers("/usuarios/update-profile").hasAuthority("ADMIN")

                        // CATEGORIAS
                        .requestMatchers("/categorias/all-active").hasAnyAuthority("SUPERADMIN", "ADMIN")
                        .requestMatchers("/categorias/").hasAnyAuthority("SUPERADMIN", "ADMIN")
                        .requestMatchers("/categorias/all").hasAuthority("SUPERADMIN")
                        .requestMatchers("/categorias/save").hasAuthority("SUPERADMIN")
                        .requestMatchers("/categorias/update").hasAuthority("SUPERADMIN")
                        .requestMatchers("/categorias/change-status").hasAuthority("SUPERADMIN")

                        // EVENTOS
                        .requestMatchers("/eventos/all-active").permitAll()
                        .requestMatchers("/eventos/all-ordered").permitAll()
                        .requestMatchers("/eventos/").permitAll()
                        .requestMatchers("/eventos/all").hasAuthority("ADMIN")
                        .requestMatchers("/eventos/save").hasAuthority("ADMIN")
                        .requestMatchers("/eventos/update").hasAuthority("ADMIN")
                        .requestMatchers("/eventos/change-status").hasAuthority("ADMIN")

                        // PARTICIPANTE
                        .requestMatchers("/participantes/all").hasAuthority("ADMIN")
                        .requestMatchers("/participantes/save").hasAuthority("ADMIN")
                        .requestMatchers("/participantes/").hasAuthority("ADMIN")
                        .requestMatchers("/participantes/get-by-eventId/{id}").hasAuthority("ADMIN")
                        .requestMatchers("/participantes/update").hasAuthority("ADMIN")
                        .requestMatchers("/participantes/change-status").hasAuthority("ADMIN")

                        .requestMatchers("/user/send-email").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        .requestMatchers("/user/verify-code").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5501", "http://localhost:5501")); // Orígenes permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados
        configuration.setAllowCredentials(true); // Permitir credenciales como cookies o tokens

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
