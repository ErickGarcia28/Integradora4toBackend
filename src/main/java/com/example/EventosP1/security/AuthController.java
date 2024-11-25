package com.example.EventosP1.security;

import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.usuario.model.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreoElectronico(loginRequest.getCorreo());

        // Verifica si el usuario existe
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Verifica la contraseña
            if (passwordEncoder.matches(loginRequest.getContrasena(), usuario.getContrasena())) {
                String token = jwtUtil.generateToken(usuario.getCorreoElectronico(), usuario.getRol().name());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Credenciales inválidas: Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas: Usuario no encontrado");
        }
    }


}
