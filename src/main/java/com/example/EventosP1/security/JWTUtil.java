package com.example.EventosP1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String correoElectronico, String rol, Long id, boolean status) {
        return Jwts.builder()
                .setSubject(correoElectronico)  // Usamos el correo como el "subject"
                .claim("role", rol)  // Incluimos el rol
                .claim("id", id)  // Incluimos el ID del usuario
                .claim("status", status)  // Incluimos el estado (activo o inactivo)
                .setIssuedAt(new Date())  // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))  // Expiración
                .signWith(secretKey)  // Clave secreta para firmar el token
                .compact();
    }



    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        String role = claims.get("role", String.class);
        System.out.println(role);
        System.out.println("Rol extraído del token: " + role); // Línea de depuración
        return role;
    }

    public Long extractId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);  // Cambiar a Long.class
    }


    public boolean extractStatus(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("status", Boolean.class);  // Extraemos el estado del usuario
    }


}
