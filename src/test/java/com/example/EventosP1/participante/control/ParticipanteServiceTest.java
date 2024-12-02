package com.example.EventosP1.participante.control;

import com.example.EventosP1.evento.model.Evento;
import com.example.EventosP1.evento.model.EventoRepository;
import com.example.EventosP1.participante.model.ParticipanteDTO;
import com.example.EventosP1.participante.model.Participante;
import com.example.EventosP1.participante.model.ParticipanteRepository;
import com.example.EventosP1.usuario.model.Usuario;
import com.example.EventosP1.categoria.model.Categoria;
import com.example.EventosP1.utils.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;
    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    private ParticipanteDTO participanteDTO;
    private Evento evento;
    private Usuario usuario;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario("Juan", "Pérez", "juanperez@gmail.com", 1234567890L, "password", null);
        categoria = new Categoria("Categoría de prueba", "Descripción de la categoría");

        evento = new Evento("Evento de Prueba", LocalDate.now(), LocalTime.now(), "Lugar de evento", categoria,  "Descripción del evento" , usuario);
        evento.setId(1L);
        participanteDTO = new ParticipanteDTO();
        participanteDTO.setNombre("Jane");
        participanteDTO.setApellido("Doe");
        participanteDTO.setCorreoElectronico("example05@gmail.com");
        participanteDTO.setTelefono(7771523546L);
        participanteDTO.setDireccion("Río Tamazula no. 13 Vista Hermosa");
        participanteDTO.setEventoId(1L);
        participanteDTO.setStatus(true);
    }

    @Test
    void registrarParticipanteCP_U017() {
        Participante participante = new Participante("Jane", "Doe", 7771523546L, "example05@gmail.com", evento, "Río Tamazula no. 13 Vista Hermosa");
        when(participanteRepository.findAll()).thenReturn(List.of(participante));

        ResponseEntity<Message> response = participanteService.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody().getResult()).size());
        assertEquals("Listado de participantes", response.getBody().getText());
    }

    @Test
    void registrarParticipanteConCorreoYaExistenteCP_U018() {
        Participante participanteExistente = new Participante("Jane", "Doe", 7771523546L, "example05@gmail.com", evento, "Río Tamazula no. 13 Vista Hermosa");
        when(participanteRepository.findByCorreoElectronico("example05@gmail.com")).thenReturn(Optional.of(participanteExistente));

        ResponseEntity<Message> response = participanteService.save(participanteDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ya hay un participante con ese correo", response.getBody().getText());
    }
    @Test
    void registrarParticipanteConTelefonoYaExistenteCP_U019() {
        Participante participanteExistente = new Participante("Jane", "Doe", 7771523546L, "example05@gmail.com", evento, "Río Tamazula no. 13 Vista Hermosa");
        when(participanteRepository.findByTelefono(7771523546L)).thenReturn(Optional.of(participanteExistente));

        ResponseEntity<Message> response = participanteService.save(participanteDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El teléfono ya está registrado", response.getBody().getText());
    }

    @Test
    void registrarParticipanteConCampoVacioCP_U020() {
        participanteDTO.setNombre("");

        ResponseEntity<Message> response = participanteService.save(participanteDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No puede haber campos vacíos", response.getBody().getText());
    }

    @Test
    void registrarParticipanteAUnEventoInexistenteCP_U021() {
        when(eventoRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Message> response = participanteService.save(participanteDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Evento no encontrado", response.getBody().getText());
    }
}
