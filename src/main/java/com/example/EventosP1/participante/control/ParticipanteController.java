package com.example.EventosP1.participante.control;

import com.example.EventosP1.participante.model.ParticipanteDTO;
import com.example.EventosP1.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participantes")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @Autowired
    public ParticipanteController(ParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> getAllParticipants() {
        return participanteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getByiD(@PathVariable int id) {
        return participanteService.findById(id);
    }

    @GetMapping("/get-by-eventId/{id}")
    public ResponseEntity<Message> getAllParticipantsByEventId(@PathVariable long id) {
        return participanteService.findAllByEvento(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> saveParticipant(@Validated(ParticipanteDTO.Register.class) @RequestBody ParticipanteDTO dto) {
        return participanteService.save(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateParticipant(@Validated(ParticipanteDTO.Modify.class) @RequestBody ParticipanteDTO dto) {
        return participanteService.update(dto);
    }

    @PutMapping("/change-status")
    public ResponseEntity<Message> changeStatus(@Validated(ParticipanteDTO.ChangeStatus.class) @RequestBody ParticipanteDTO dto) {
        return participanteService.changeStatus(dto);
    }
}
