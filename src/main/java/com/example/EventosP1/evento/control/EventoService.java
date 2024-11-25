package com.example.EventosP1.evento.control;

import com.example.EventosP1.categoria.model.Categoria;
import com.example.EventosP1.categoria.model.CategoriaRepository;
import com.example.EventosP1.evento.model.*;
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
public class EventoService {

    private static final Logger logger = LoggerFactory.getLogger(EventoService.class); // Logger

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository, CategoriaRepository categoriaRepository) {
        this.eventoRepository = eventoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // CONSULTAR EVENTOS
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        logger.info("Iniciando búsqueda de todos los eventos.");
        List<Evento> eventos = eventoRepository.findAll();
        logger.info("Se encontraron {} eventos.", eventos.size());
        return new ResponseEntity<>(new Message(eventos, "Listado de eventos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CONSULTAR EVENTOS ACTIVOS
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllActive() {
        logger.info("Iniciando búsqueda de todos los eventos activos.");
        List<Evento> eventos = eventoRepository.findAllByStatusIsTrue();
        logger.info("Se encontraron {} eventos activos.", eventos.size());
        return new ResponseEntity<>(new Message(eventos, "Listado de eventos activos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // REGISTRAR UN EVENTO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(EventoDTO dto) {
        logger.info("Iniciando registro de nuevo evento con nombre: {}", dto.getNombre());

        if(dto.getNombre().length() > 200) {
            logger.warn("El nombre del evento excede los 200 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getLugar().length() > 250) {
            logger.warn("El lugar del evento excede los 250 caracteres.");
            return new ResponseEntity<>(new Message("El lugar excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        if(dto.getDescripcioin().length() > 300) {
            logger.warn("La descripcion del evento excede los 300 caracteres.");
            return new ResponseEntity<>(new Message("La descripcion excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Verificar si ya existe un evento con el mismo nombre
        Optional<Evento> eventoExistente = eventoRepository.findByNombre(dto.getNombre());
        if (eventoExistente.isPresent()) {
            logger.warn("Ya existe un evento con el nombre '{}'.", dto.getNombre());
            return new ResponseEntity<>(new Message("Ya existe un evento con el mismo nombre", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(dto.getCategoriaId());
        if (!categoriaOptional.isPresent()) {
            logger.warn("Categoría con ID {} no encontrada.", dto.getCategoriaId());
            return new ResponseEntity<>(new Message("Categoría no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Evento evento = new Evento(dto.getNombre(), dto.getFecha(), dto.getLugar(), categoriaOptional.get(), dto.getDescripcioin());
        evento = eventoRepository.saveAndFlush(evento);
        logger.info("Evento con nombre '{}' registrado exitosamente.", evento.getNombre());

        return new ResponseEntity<>(new Message(evento, "Registro exitoso", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    // ACTUALIZAR UN EVENTO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(EventoDTO dto) {
        logger.info("Iniciando actualización de evento con ID: {}", dto.getId());

        Optional<Evento> eventoOptional = eventoRepository.findById(dto.getId());
        if (!eventoOptional.isPresent()) {
            logger.warn("Evento con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Evento no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        if(dto.getNombre().length() > 200) {
            logger.warn("El nombre del evento excede los 200 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getLugar().length() > 250) {
            logger.warn("El lugar del evento excede los 250 caracteres.");
            return new ResponseEntity<>(new Message("El lugar excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getDescripcioin().length() > 300) {
            logger.warn("La descripcion del evento excede los 300 caracteres.");
            return new ResponseEntity<>(new Message("La descripcion excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(dto.getCategoriaId());
        if (!categoriaOptional.isPresent()) {
            logger.warn("Categoría con ID {} no encontrada.", dto.getCategoriaId());
            return new ResponseEntity<>(new Message("Categoría no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Evento evento = eventoOptional.get();
        evento.setNombre(dto.getNombre());
        evento.setFecha(dto.getFecha());
        evento.setLugar(dto.getLugar());
        evento.setCategoria(categoriaOptional.get());
        evento.setDescripcion(dto.getDescripcioin());

        evento = eventoRepository.saveAndFlush(evento);
        logger.info("Evento con ID {} actualizado exitosamente.", evento.getId());

        return new ResponseEntity<>(new Message(evento, "Actualización exitosa", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CAMBIAR ESTADO DE UN EVENTO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(EventoDTO dto) {
        logger.info("Iniciando cambio de estado de evento con ID: {}", dto.getId());

        Optional<Evento> eventoOptional = eventoRepository.findById(dto.getId());
        if (!eventoOptional.isPresent()) {
            logger.warn("Evento con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Evento no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Evento evento = eventoOptional.get();
        evento.setStatus(!evento.isStatus());
        eventoRepository.saveAndFlush(evento);
        logger.info("Estado de evento con ID {} actualizado a {}.", evento.getId(), evento.isStatus());

        return new ResponseEntity<>(new Message(evento, "Estado actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
