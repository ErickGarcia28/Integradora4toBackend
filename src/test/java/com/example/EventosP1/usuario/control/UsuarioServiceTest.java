//package com.example.EventosP1.usuario.control;
//
//import com.example.EventosP1.usuario.control.UsuarioService;
//import com.example.EventosP1.usuario.model.Usuario;
//import com.example.EventosP1.usuario.model.UsuarioDTO;
//import com.example.EventosP1.usuario.model.UsuarioRepository;
//import com.example.EventosP1.utils.Message;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class UsuarioServiceTest {
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @InjectMocks
//    private UsuarioService usuarioService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void saveUser_ValidData_UserCreated1() {
//        // Arrange: crear un objeto UsuarioDTO con datos válidos
//        UsuarioDTO dto = new UsuarioDTO();
//        dto.setNombre("Juan");
//        dto.setApellido("Pérez");
//        dto.setCorreoElectronico("juan.perez@example.com");
//        dto.setTelefono(1234567890);
//        dto.setContrasena("password123");
//
//
////        // Simulación: se configura el repositorio para devolver un objeto Usuario cuando se llame a saveAndFlush()
////        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act: invocar el método save de usuarioService
//        ResponseEntity<Message> response = usuarioService.save(dto);
//
//        // Assert: verificar que la respuesta es la esperada
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals("Registro exitoso", response.getBody().getText());
//
////        // Verificar que el método saveAndFlush en el repositorio fue llamado exactamente una vez
////        verify(usuarioRepository, times(1)).saveAndFlush(any(Usuario.class));
//    }
//
//
//    @Test
//    void saveUser_DuplicateEmail_UserNotCreated2() {
//        // Arrange: crear un objeto UsuarioDTO con datos que contengan un correo ya existente
//        UsuarioDTO dto = new UsuarioDTO();
//        dto.setNombre("Jane");
//        dto.setApellido("Doe");
//        dto.setCorreoElectronico("example05@gmail.com"); // Correo duplicado
//        dto.setTelefono(77715546);
//        dto.setContrasena("12345678");
//        dto.setRol("USER");
//        dto.setStatus(true);
//
//        // Simulación de existsByCorreoElectronico para retornar true, indicando que el correo ya existe
//        when(usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())).thenReturn(true);
//
//        // Act: invocar el método save de usuarioService
//        ResponseEntity<Message> response = usuarioService.save(dto);
//
//        // Assert: verificar que la respuesta tenga el estado de error esperado
//        // Cambiar la expectativa a 400
//        assertEquals(400, response.getStatusCodeValue());
//        assertEquals("El correo electrónico ya está en uso", response.getBody().getText());
//
//        // Verificar que el método saveAndFlush NO se haya llamado ya que no debe guardar un usuario duplicado
////        verify(usuarioRepository, never()).saveAndFlush(any(Usuario.class));
//    }
//
//    // FALTA EL DE TELÉFONO DUPLICADO
//
//
//    @Test
//    void verQueNoEsteVacioElnombre4() {
//        // Arrange: crear un objeto UsuarioDTO con datos que contengan un correo ya existente
//        UsuarioDTO dto = new UsuarioDTO();
//        dto.setNombre("");
//        dto.setApellido("Doe");
//        dto.setCorreoElectronico("example05@gmail.com"); // Correo duplicado
//        dto.setTelefono(777732);
//        dto.setContrasena("12345678");
//        dto.setRol("USER");
//        dto.setStatus(true);
//
//        // Act: invocar el método save de usuarioService
//        ResponseEntity<Message> response = usuarioService.save(dto);
//
//        // Assert: verificar que la respuesta tenga el estado de error esperado
//        // Cambiar la expectativa a 400
//        assertEquals(400, response.getStatusCodeValue());
//        assertEquals("El nombre no puede estar vacio", response.getBody().getText());
//
//        // Verificar que el método saveAndFlush NO se haya llamado ya que no debe guardar un usuario duplicado
////        verify(usuarioRepository, never()).saveAndFlush(any(Usuario.class));
//    }
//
//
//}
