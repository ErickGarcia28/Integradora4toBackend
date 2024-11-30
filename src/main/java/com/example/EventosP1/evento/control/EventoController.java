package com.example.EventosP1.evento.control;

import com.example.EventosP1.evento.model.EventoDTO;
import com.example.EventosP1.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAllEvents() {
        return eventoService.findAll();
    }

    @GetMapping("/all-ordered")
    public ResponseEntity<Message> getAllOrderedEvents() {
        return eventoService.findOrderByFechaAsc();
    }

    @GetMapping("/all-by-user-id/{userId}")
    public ResponseEntity<Message> getAllByUserId(@PathVariable long userId) {
        return eventoService.findAllByUsuarioId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getEventById(@PathVariable long id) {
        return eventoService.findById(id);
    }

    @GetMapping("/all-active")
    public ResponseEntity<Message> getAllActiveEvents() {
        return eventoService.findAllActive();
    }
    @PostMapping("/save")
    public ResponseEntity<Message> saveEvent(@Validated(EventoDTO.Register.class) @RequestBody EventoDTO dto) {
        return eventoService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateEvent(@Validated(EventoDTO.Modify.class) @RequestBody EventoDTO dto) {
        return eventoService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(EventoDTO.ChangeStatus.class) @RequestBody EventoDTO dto) {
        return eventoService.changeStatus(dto);
    }
}
