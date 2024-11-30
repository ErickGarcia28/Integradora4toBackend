package com.example.EventosP1.categoria.control;

import com.example.EventosP1.categoria.model.CategoriaDTO;
import com.example.EventosP1.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAllCategories() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getCategoryById(@PathVariable long id) {
        return categoriaService.findById(id);
    }

    @GetMapping("/all-active")
    public ResponseEntity<Message> getAllActiveCategories() {
        return categoriaService.findAllActive();
    }

    @PostMapping("/save")
    public ResponseEntity<Message> saveCategory(@Validated(CategoriaDTO.Register.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateCategory(@Validated(CategoriaDTO.Modify.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(CategoriaDTO.ChangeStatus.class) @RequestBody CategoriaDTO dto) {
        return categoriaService.changeStatus(dto);
    }
}
