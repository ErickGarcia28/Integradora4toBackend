package com.example.EventosP1.categoria.control;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.EventosP1.categoria.model.Categoria;
import com.example.EventosP1.categoria.model.CategoriaDTO;
import com.example.EventosP1.categoria.model.CategoriaRepository;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoriaService = new CategoriaService(categoriaRepository);
    }

    @Test
    void testConsultarCategoriasCP_U012() {
        Categoria categoria1 = new Categoria("Música", "Conecta a las personas a través del ritmo");
        Categoria categoria2 = new Categoria("Deportes", "Eventos deportivos para todos");
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);

        when(categoriaRepository.findAll()).thenReturn(categorias);

        ResponseEntity<Message> response = categoriaService.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Listado de categorías", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }

    @Test
    void testConsultarCategoriasActivasCP_U013() {
        // Datos de prueba
        Categoria categoria1 = new Categoria("Música", "Conecta a las personas a través del ritmo");
        categoria1.setStatus(true);
        Categoria categoria2 = new Categoria("Deportes", "Eventos deportivos para todos");
        categoria2.setStatus(true);
        List<Categoria> categoriasActivas = Arrays.asList(categoria1, categoria2);

        when(categoriaRepository.findAllByStatusIsTrue()).thenReturn(categoriasActivas);

        ResponseEntity<Message> response = categoriaService.findAllActive();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Listado de categorías activas", response.getBody().getText());
        assertEquals(2, ((List<?>) response.getBody().getResult()).size());
    }


    @Test
    void testRegistrarCategoriaCP_U014() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Música");
        categoriaDTO.setDescripcion("Conecta a las personas a través del ritmo");

        Categoria categoria = new Categoria("Música", "Conecta a las personas a través del ritmo");
        when(categoriaRepository.saveAndFlush(any(Categoria.class))).thenReturn(categoria);

        ResponseEntity<Message> response = categoriaService.save(categoriaDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Registro exitoso", response.getBody().getText());
    }

    @Test
    void testRegistrarCategoriaConCampoVacioCP_U015() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("");
        categoriaDTO.setDescripcion("Descubre nuevos sabores con los eventos de repostería");
        ResponseEntity<Message> response = categoriaService.save(categoriaDTO);

        assertEquals(400, response.getStatusCodeValue());  // Asegurarse que es BAD_REQUEST
        assertNotNull(response.getBody());
        assertEquals("El nombre no puede estar vacío", response.getBody().getText());  // El mensaje correcto
    }


    @Test
    void testRegistrarCategoriaRepetidaCP_U016() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Música");
        categoriaDTO.setDescripcion("Conecta a las personas a través del ritmo");

        Categoria categoriaExistente = new Categoria("Música", "Conecta a las personas a través del ritmo");
        when(categoriaRepository.findByNombre("Música")).thenReturn(Optional.of(categoriaExistente));

        ResponseEntity<Message> response = categoriaService.save(categoriaDTO);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("El nombre de esa categoría ya está en uso", response.getBody().getText());
    }

}
