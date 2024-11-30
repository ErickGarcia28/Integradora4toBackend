package com.example.EventosP1.security;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtención de la ruta de la solicitud para evitar aplicar el filtro a la ruta de login
        String path = request.getRequestURI();
        if (path.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el encabezado de autorización
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Verificar que el encabezado de autorización esté presente y empiece con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                // Extraemos el nombre de usuario desde el token
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Username extraído del token: " + username);
            } catch (IllegalArgumentException | MalformedJwtException e) {
                logger.error("Token JWT inválido: " + e.getMessage());
            }
        }

        // Si el username no es nulo y no hay autenticación previa en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                // Extraer el rol, id y status del token
                String role = jwtUtil.extractRole(jwt);
                Long userId = jwtUtil.extractId(jwt);  // Extraer el ID
                boolean userStatus = jwtUtil.extractStatus(jwt);  // Extraer el status

                System.out.println("Rol extraído del token: " + role);
                System.out.println("ID extraído del token: " + userId);
                System.out.println("Estado extraído del token: " + userStatus);

                // Verificar si el usuario está activo
                if (!userStatus) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario deshabilitado");
                    return;
                }

                // Crear la autoridad a partir del rol extraído
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // Configurar la autenticación con el rol extraído y otros detalles
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Autenticación exitosa para el usuario: " + username);
            } else {
                System.out.println("Token JWT inválido para el usuario: " + username);
            }
        }

        // Continuar con el siguiente filtro en la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
