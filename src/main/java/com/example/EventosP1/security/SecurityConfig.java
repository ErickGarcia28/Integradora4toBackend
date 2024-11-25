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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()

                        // USUARIOS
                        // Recordar la expiracion del token (actualmente tiene 24hrs)
                        .requestMatchers("/usuarios/all").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/usuarios/save").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/usuarios/update").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/usuarios/change-status").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/usuarios/update-profile").hasAuthority("ADMIN") // se probo y paso

                        // (recordar iniciar sesion y meter los tokens en las peticiones para probar)

                        // CATEGORIAS
                        .requestMatchers("/categorias/all-active").hasAnyAuthority("SUPERADMIN","ADMIN") // se probo y paso
                        .requestMatchers("/categorias/all").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/categorias/save").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/categorias/update").hasAuthority("SUPERADMIN") // se probo y paso
                        .requestMatchers("/categorias/change-status").hasAuthority("SUPERADMIN") // se probo y paso

                        // EVENTOS
                        .requestMatchers("/eventos/all-active").permitAll() // se probo y paso
                        .requestMatchers("/eventos/all").hasAuthority("ADMIN") // se probo y paso
                        .requestMatchers("/eventos/save").hasAuthority("ADMIN") // se probo y paso. (El categoriaId es un atributo, no un objeto) (revisar el DTO)
                        .requestMatchers("/eventos/update").hasAuthority("ADMIN") // se probo y paso
                        .requestMatchers("/eventos/change-status").hasAuthority("ADMIN") // se probo y paso

                        // PARTICIPANTE
                        .requestMatchers("/participantes/all").hasAuthority("ADMIN") // se probo y paso
                        .requestMatchers("/participantes/save").hasAuthority("ADMIN") // se probo y paso
                        .requestMatchers("/participantes/update").hasAuthority("ADMIN") // se probo y paso
                        .requestMatchers("/participantes/change-status").hasAuthority("ADMIN") // se probo y paso

                        .requestMatchers("/user/send-email").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        .requestMatchers("/user/verify-code").hasAnyAuthority("ADMIN", "SUPERADMIN")


//                        .requestMatchers("/usuarios/all").hasRole("SUPERADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
