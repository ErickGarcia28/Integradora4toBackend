package com.example.EventosP1;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Permitir CORS para todas las rutas
                .allowedOrigins("*")  // Permitir cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Permitir todos los métodos HTTP
                .allowedHeaders("*")  // Permitir todos los encabezados
                .allowCredentials(false);  // Deshabilitar credenciales si no son necesarias
    }
}
