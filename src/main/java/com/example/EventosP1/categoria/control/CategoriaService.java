package com.example.EventosP1.categoria.control;

import com.example.EventosP1.categoria.model.*;
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
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class); // Logger

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // CONSULTAR CATEGORIAS
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        logger.info("Iniciando búsqueda de todas las categorías.");
        List<Categoria> categorias = categoriaRepository.findAll();
        logger.info("Se encontraron {} categorías.", categorias.size());
        return new ResponseEntity<>(new Message(categorias, "Listado de categorías", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CONSULTAR CATEGORIAS ACTIVAS
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllActive() {
        logger.info("Iniciando búsqueda de todas las categorías activas.");
        List<Categoria> categorias = categoriaRepository.findAllByStatusIsTrue();
        logger.info("Se encontraron {} categorías activas.", categorias.size());
        return new ResponseEntity<>(new Message(categorias, "Listado de categorías activas", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CONSULTAR REGISTRAR CATEGORÍA
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(CategoriaDTO dto) {
        logger.info("Iniciando registro de nueva categoría con nombre: {}", dto.getNombre());

        if(dto.getNombre().length() > 200) {
            logger.warn("El nombre de la categoría excede los 200 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getDescripcion().length() > 300) {
            logger.warn("La descripción de la categoría excede los 300 caracteres.");
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Categoria categoria = new Categoria(dto.getNombre(), dto.getDescripcion());
        categoria = categoriaRepository.saveAndFlush(categoria);
        logger.info("Categoría con nombre '{}' registrada exitosamente.", categoria.getNombre());

        return new ResponseEntity<>(new Message(categoria, "Registro exitoso", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    // ACTUALIZAR UNA CATEGORÍA
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(CategoriaDTO dto) {
        logger.info("Iniciando actualización de la categoría con ID: {}", dto.getId());

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(dto.getId());
        if (!categoriaOptional.isPresent()) {
            logger.warn("Categoría con ID {} no encontrada.", dto.getId());
            return new ResponseEntity<>(new Message("Categoría no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        if(dto.getNombre().length() > 200) {
            logger.warn("El nombre de la categoría excede los 200 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getDescripcion().length() > 300) {
            logger.warn("La descripción de la categoría excede los 300 caracteres.");
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Categoria categoria = categoriaOptional.get();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        categoria = categoriaRepository.saveAndFlush(categoria);
        logger.info("Categoría con ID {} actualizada exitosamente.", categoria.getId());

        return new ResponseEntity<>(new Message(categoria, "Actualización exitosa", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CAMBIAR EL STATUS DE UNA CATEGORÍA
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(CategoriaDTO dto) {
        logger.info("Iniciando cambio de estado de la categoría con ID: {}", dto.getId());

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(dto.getId());
        if (!categoriaOptional.isPresent()) {
            logger.warn("Categoría con ID {} no encontrada.", dto.getId());
            return new ResponseEntity<>(new Message("Categoría no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Categoria categoria = categoriaOptional.get();
        categoria.setStatus(!categoria.isStatus());
        categoriaRepository.saveAndFlush(categoria);
        logger.info("Estado de la categoría con ID {} actualizado a {}.", categoria.getId(), categoria.isStatus());

        return new ResponseEntity<>(new Message(categoria, "Estado actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
