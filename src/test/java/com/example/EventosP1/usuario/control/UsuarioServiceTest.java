package com.example.EventosP1.usuario.control;

import com.example.EventosP1.usuario.model.*;
import com.example.EventosP1.utils.EmailSender;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO validUsuarioDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        validUsuarioDTO = new UsuarioDTO();
        validUsuarioDTO.setId(1L);
        validUsuarioDTO.setNombre("Juan");
        validUsuarioDTO.setApellido("Perez");
        validUsuarioDTO.setCorreoElectronico("juan.perez@example.com");
        validUsuarioDTO.setTelefono(1234567890);
        validUsuarioDTO.setContrasena("password123");
        validUsuarioDTO.setStatus(true);
    }

    @Test
    public void testRegistrarUsuarioSuccessCP_U001() {
        when(usuarioRepository.existsByCorreoElectronico(validUsuarioDTO.getCorreoElectronico())).thenReturn(false);
        when(passwordEncoder.encode(validUsuarioDTO.getContrasena())).thenReturn("encodedPassword");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenReturn(new Usuario(validUsuarioDTO.getNombre(), validUsuarioDTO.getApellido(), validUsuarioDTO.getCorreoElectronico(), validUsuarioDTO.getTelefono(), "encodedPassword", Rol.ADMIN));

        ResponseEntity<Message> response = usuarioService.save(validUsuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro exitoso", response.getBody().getText());
    }

    @Test
    public void testRegistrarUsuarioCorreoExistenteCP_U002() {
        when(usuarioRepository.existsByCorreoElectronico(validUsuarioDTO.getCorreoElectronico())).thenReturn(true);
        ResponseEntity<Message> response = usuarioService.save(validUsuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El correo electrónico ya está en uso", response.getBody().getText());
    }

    @Test
    public void testRegistrarUsuarioConCamposVaciosCP_U004() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(" ");
        usuarioDTO.setApellido("Doe");
        usuarioDTO.setCorreoElectronico("johndoe@example.com");
        usuarioDTO.setTelefono(7771523544L);
        usuarioDTO.setContrasena("12345678");

        ResponseEntity<Message> response = usuarioService.save(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre no puede estar vacio", response.getBody().getText());
    }

    @Test
    public void testRegistrarUsuarioTelefonoInvalidoCP_U005() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Jane");
        usuarioDTO.setApellido("Doe");
        usuarioDTO.setCorreoElectronico("johndoe@example.com");
        usuarioDTO.setTelefono(777732);
        usuarioDTO.setContrasena("12345678");

        ResponseEntity<Message> response = usuarioService.save(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El teléfono debe tener exactamente 10 dígitos", response.getBody().getText());
    }

    @Test
    public void testRegistrarUsuarioConContrasenaInvalidaCP_U006() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Jane");
        usuarioDTO.setApellido("Doe");
        usuarioDTO.setCorreoElectronico("johndoe@example.com");
        usuarioDTO.setTelefono(7771486939L);
        usuarioDTO.setContrasena("123");

        ResponseEntity<Message> response = usuarioService.save(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La contraseña debe tener por lo menos 8 caracteres", response.getBody().getText());
    }

}
