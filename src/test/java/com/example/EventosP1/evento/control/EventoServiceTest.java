package com.example.EventosP1.evento.control;

import com.example.EventosP1.evento.model.Evento;
import com.example.EventosP1.evento.model.EventoDTO;
import com.example.EventosP1.evento.model.EventoRepository;
import com.example.EventosP1.usuario.control.UsuarioService;
import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.utils.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    private EventoRepository eventoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Caso de Prueba: CP-U007 - Registrar un evento
    @Test
    void testRegistrarEventoCP_U007() {
        // Crear un usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");

        // Configurar el mock para que devuelva el usuario cuando se le pase el ID 1


        // Crear el objeto EventoDTO
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Vida fest");
        eventoDTO.setFecha(LocalDate.of(2024, 10, 23));
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        // Configurar el mock para que devuelva un evento al registrar
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre(eventoDTO.getNombre());
        evento.setFecha(eventoDTO.getFecha());
        evento.setHora(eventoDTO.getHora());
        evento.setLugar(eventoDTO.getLugar());
        evento.setStatus(eventoDTO.isStatus());
        evento.setId(eventoDTO.getUsuarioId());
        evento.setDescripcion(eventoDTO.getDescripcion());

        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Registro exitoso", response.getBody().getText());

        // Verificar que los métodos de los mocks fueron llamados
        verify(usuarioService).findById(1L);
        verify(eventoRepository).save(any(Evento.class));
    }

    // Caso de Prueba: CP-U008 - Registrar un evento con un campo vacío
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

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre no puede estar vacío.", response.getBody().getText());
    }

    // Caso de Prueba: CP-U009 - Registrar un evento con fecha inválida
    @Test
    void testRegistrarEventoConFechaInvalidaCP_U009() {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Vida fest");
        eventoDTO.setFecha(LocalDate.of(1900, 10, 23)); // Fecha inválida
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La fecha del evento no es válida.", response.getBody().getText());
    }

    // Caso de Prueba: CP-U010 - Registrar un evento con categoría inexistente
    @Test
    void testRegistrarEventoConCategoriaInexistenteCP_U010() {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Vida fest");
        eventoDTO.setFecha(LocalDate.of(2024, 10, 23));
        eventoDTO.setHora(LocalTime.of(18, 0));
        eventoDTO.setLugar("Calle 125, 24 Tulipanes");
        eventoDTO.setStatus(true);
        eventoDTO.setUsuarioId(1L);
        eventoDTO.setDescripcion("Descripción del evento");

        // Se asume que la categoría no existe, no necesitamos mockear el comportamiento de la categoría
        eventoDTO.setCategoriaId(999L); // ID de categoría inexistente

        // Llamar al servicio para registrar el evento
        ResponseEntity<Message> response = eventoService.save(eventoDTO);

        // Verificar el resultado
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Categoría no encontrada.", response.getBody().getText());
    }

    // Caso de Prueba: CP-U011 - Consultar eventos activos
    @Test
    void testConsultarEventosActivosCP_U011() {
        // Crear un evento activo simulado
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Evento Activo");
        evento.setStatus(true);

        // Configurar el mock para que retorne el evento activo
        when(eventoRepository.findAllByStatusIsTrue()).thenReturn(java.util.Collections.singletonList(evento));

        // Llamar al servicio para obtener los eventos activos
        ResponseEntity<Message> response = eventoService.findAllActive();

        // Verificar el resultado
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Listado de eventos activos", response.getBody().getText());

        // Verificar que el método findByStatus fue llamado
        verify(eventoRepository).findAllByStatusIsTrue();
    }
}
