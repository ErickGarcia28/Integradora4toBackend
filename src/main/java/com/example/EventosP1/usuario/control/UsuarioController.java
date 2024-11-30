package com.example.EventosP1.usuario.control;

import com.example.EventosP1.usuario.model.PerfilDTO;
import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.usuario.model.UsuarioDTO;
import com.example.EventosP1.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

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

    // RECUPERACIÓN DE LA CONTRASEÑA
    @PostMapping("/send-email")
    public ResponseEntity<Message> sendEmail(@Validated({UsuarioDTO.FindByEmail.class}) @RequestBody Usuario usuario){
        return usuarioService.sendEmail(usuario);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Message> verifyCode(@Validated({UsuarioDTO.VerifyCode.class}) @RequestBody Usuario usuario){
        return usuarioService.verifyCode(usuario);
    }

}
