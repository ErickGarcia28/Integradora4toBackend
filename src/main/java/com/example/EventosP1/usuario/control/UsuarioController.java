package com.example.EventosP1.usuario.control;

import com.example.EventosP1.usuario.model.PerfilDTO;
import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.usuario.model.UsuarioDTO;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAllUsers() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getById(@PathVariable long id) {
        return usuarioService.findById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> saveUser(@Validated(UsuarioDTO.Register.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateUser(@Validated(UsuarioDTO.Modify.class) @RequestBody PerfilDTO dto) {
        return usuarioService.update(dto);
    }
    @PutMapping("/update-profile")
    public ResponseEntity<Message> updateProfile(@Validated(PerfilDTO.Modify.class) @RequestBody PerfilDTO dto) {
        return usuarioService.updateProfile(dto);
    }
    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(UsuarioDTO.ChangeStatus.class) @RequestBody UsuarioDTO dto) {
        return usuarioService.changeStatus(dto);
    }
    @PostMapping("/send-email/{correo}")
    public ResponseEntity<Message> sendEmail(@PathVariable("correo") String correo) {
        if (correo == null || correo.isEmpty()) {
            return new ResponseEntity<>(new Message("El correo no puede estar vacío", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.sendEmail(correo);
    }

    @PostMapping("/verify-code/{codigo}")
    public ResponseEntity<Message> verifyCode(@PathVariable("codigo") String codigo) {
        return usuarioService.verifyCode( codigo);
    }

    @PutMapping("/update-password")
    public ResponseEntity<Message> updatePassword(@RequestBody Map<String, String> payload) {
        String correo = payload.get("correoElectronico");
        String nuevaContrasena = payload.get("nuevaContrasena");

        if (correo == null || nuevaContrasena == null) {
            return new ResponseEntity<>(new Message("Faltan parámetros", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        return usuarioService.updatePassword(correo, nuevaContrasena);
    }



}
