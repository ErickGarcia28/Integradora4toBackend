package com.example.EventosP1.participante.control;

import com.example.EventosP1.evento.model.Evento;
import com.example.EventosP1.evento.model.EventoRepository;
import com.example.EventosP1.participante.model.*;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ParticipanteService {

    private static final Logger logger = LoggerFactory.getLogger(ParticipanteService.class); // Logger

    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;

    @Autowired
    public ParticipanteService(ParticipanteRepository participanteRepository, EventoRepository eventoRepository) {
        this.participanteRepository = participanteRepository;
        this.eventoRepository = eventoRepository;
    }

    // CONSULTAR PARTICIPANTES
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        logger.info("Iniciando búsqueda de todos los participantes.");
        List<Participante> participantes = participanteRepository.findAll();
        logger.info("Se encontraron {} participantes.", participantes.size());
        return new ResponseEntity<>(new Message(participantes, "Listado de participantes", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // REGISTRAR PARTICIPANTE
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(ParticipanteDTO dto) {
        logger.info("Iniciando registro de nuevo participante con nombre: {}", dto.getNombre());

        if(dto.getNombre().length() > 50) {
            logger.warn("El nombre del participante excede los 50 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getApellido().length() > 80) {
            logger.warn("El apellido del participante excede los 80 caracteres.");
            return new ResponseEntity<>(new Message("El apellido excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getCorreoElectronico().length() > 74) {
            logger.warn("El correo electrónico del participante excede los 74 caracteres.");
            return new ResponseEntity<>(new Message("El correo electrónico excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getDireccion().length() > 250) {
            logger.warn("La dirección del participante excede los 250 caracteres.");
            return new ResponseEntity<>(new Message("La dirección excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Optional<Evento> eventoOptional = eventoRepository.findById(dto.getEventoId());
        logger.info("Evento ID recibido: {}", dto.getEventoId());

        if (!eventoOptional.isPresent()) {
            logger.error("El evento con ID {} no se encontró.", dto.getEventoId());
            return new ResponseEntity<>(new Message("Evento no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Participante participante = new Participante(dto.getNombre(), dto.getApellido(), dto.getTelefono(), dto.getCorreoElectronico(), eventoOptional.get(), dto.getDireccion());
        participante = participanteRepository.saveAndFlush(participante);
        logger.info("Participante con nombre '{}' registrado exitosamente.", participante.getNombre());

        return new ResponseEntity<>(new Message(participante, "Registro exitoso", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    // ACTUALIZAR PARTICIPANTE
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(ParticipanteDTO dto) {
        logger.info("Iniciando actualización de participante con ID: {}", dto.getId());

        Optional<Participante> participanteOptional = participanteRepository.findById(dto.getId());
        if (!participanteOptional.isPresent()) {
            logger.warn("Participante con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Participante no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        if(dto.getNombre().length() > 50) {
            logger.warn("El nombre del participante excede los 50 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getApellido().length() > 80) {
            logger.warn("El apellido del participante excede los 80 caracteres.");
            return new ResponseEntity<>(new Message("El apellido excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getCorreoElectronico().length() > 74) {
            logger.warn("El correo electrónico del participante excede los 74 caracteres.");
            return new ResponseEntity<>(new Message("El correo electrónico excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getDireccion().length() > 250) {
            logger.warn("La dirección del participante excede los 250 caracteres.");
            return new ResponseEntity<>(new Message("La dirección excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Optional<Evento> eventoOptional = eventoRepository.findById(dto.getEventoId());
        if (!eventoOptional.isPresent()) {
            logger.warn("Evento con ID {} no encontrado.", dto.getEventoId());
            return new ResponseEntity<>(new Message("Evento no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Participante participante = participanteOptional.get();
        participante.setNombre(dto.getNombre());
        participante.setApellido(dto.getApellido());
        participante.setTelefono(dto.getTelefono());
        participante.setCorreoElectronico(dto.getCorreoElectronico());
        participante.setEvento(eventoOptional.get());
        participante.setDireccion(dto.getDireccion());

        participante = participanteRepository.saveAndFlush(participante);
        logger.info("Participante con ID {} actualizado exitosamente.", participante.getId());

        return new ResponseEntity<>(new Message(participante, "Actualización exitosa", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CAMBIAR ESTADO DE PARTICIPANTE
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(ParticipanteDTO dto) {
        logger.info("Iniciando cambio de estado de participante con ID: {}", dto.getId());

        Optional<Participante> participanteOptional = participanteRepository.findById(dto.getId());
        if (!participanteOptional.isPresent()) {
            logger.warn("Participante con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Participante no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Participante participante = participanteOptional.get();
        participante.setStatus(!participante.isStatus());
        participanteRepository.saveAndFlush(participante);
        logger.info("Estado del participante con ID {} actualizado a {}.", participante.getId(), participante.isStatus());

        return new ResponseEntity<>(new Message(participante, "Estado actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
