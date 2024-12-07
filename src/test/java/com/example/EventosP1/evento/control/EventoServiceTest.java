package com.example.EventosP1.evento.control;

import com.example.EventosP1.categoria.model.Categoria;
import com.example.EventosP1.categoria.model.CategoriaRepository;
import com.example.EventosP1.evento.model.Evento;
import com.example.EventosP1.evento.model.EventoDTO;
import com.example.EventosP1.evento.model.EventoRepository;
import com.example.EventosP1.usuario.control.UsuarioService;
import com.example.EventosP1.usuario.model.Rol;
import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.usuario.model.UsuarioRepository;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EventoServiceTest {

    @InjectMocks
    private EventoService eventoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testRegistrarEventoCP_U007() {
//        // Crear un usuario simulado
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//        usuario.setNombre("Usuario Test");
//        usuario.setApellido("Apellido Test");
//        usuario.setCorreoElectronico("test@example.com");
//        usuario.setTelefono(1234567890L);
//        usuario.setContrasena("securePassword");
//        usuario.setRol(Rol.ADMIN);
//
//        // Configurar el mock para usuario
//        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
//
//        // Crear una categoría simulada
//        Categoria categoria = new Categoria();
//        categoria.setId(1L);
//        categoria.setNombre("Categoría Test");
//
//        // Configurar el mock para categoría
//        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
//
//        // Crear el objeto EventoDTO
//        EventoDTO eventoDTO = new EventoDTO();
//        eventoDTO.setNombre("Vida fest");
//        eventoDTO.setFecha(LocalDate.now().plusDays(1)); // Fecha futura
//        eventoDTO.setHora(LocalTime.of(18, 0));
//        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
//        eventoDTO.setStatus(true);
//        eventoDTO.setUsuarioId(1L);
//        eventoDTO.setCategoriaId(1L);
//        eventoDTO.setDescripcion("Descripción del evento");
//
//        // Configurar el mock para guardar el evento
//        when(eventoRepository.save(any(Evento.class))).thenAnswer(invocation -> {
//            Evento evento = invocation.getArgument(0);
//            evento.setId(1L); // Simular ID generado
//            return evento;
//        });
//
//        // Llamar al servicio para registrar el evento
//        ResponseEntity<Message> response = eventoService.save(eventoDTO);
//
//        // Validar el resultado
//        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals("Registro exitoso", response.getBody().getText());
//
//        // Verificar que los mocks fueron llamados
//        verify(usuarioRepository).findById(1L);
//        verify(categoriaRepository).findById(1L);
//        verify(eventoRepository).save(any(Evento.class));
//    }

    //     Caso de Prueba: CP-U008 - Registrar un evento con un campo vacío
    @Test
    void testRegistrarEventoConCampoVacioCP_U008() {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre(""); // Nombre vacío
        eventoDTO.setFecha(LocalDate.of(2024, 10, 23));
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        // Simular que la categoría existe en el repositorio
        Categoria categoria = new Categoria();
        categoria.setId(1L); // Asumiendo que la categoría tiene ID 1
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(404, response.getStatusCodeValue());
    }


    // Caso de Prueba: CP-U009 - Registrar un evento con fecha inválida
    @Test
    void testRegistrarEventoConFechaInvalidaCP_U009() {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Vida fest");
        eventoDTO.setFecha(LocalDate.of(2028, 10, 23));
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setCategoriaId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testRegistrarEventoConCategoriaNoExistenteCP_U010() {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Vida fest");
        eventoDTO.setFecha(LocalDate.of(2024, 10, 23));
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        eventoDTO.setCategoriaId(999L);

        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Categoría no encontrada", response.getBody().getText());
    }

    @Test
    void testConsultarEventosActivosCP_U011() {
        // Crear un evento activo simulado
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Evento Activo");
        evento.setStatus(true);

        when(eventoRepository.findAllByStatusIsTrue()).thenReturn(java.util.Collections.singletonList(evento));

        ResponseEntity<Message> response = eventoService.findAllActive();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Listado de eventos activos", response.getBody().getText());

        verify(eventoRepository).findAllByStatusIsTrue();
    }
}
