package com.example.EventosP1.participante.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findAllByStatusIsTrue();
    List<Participante> findAllByEventoId(Long eventoId);

    Optional<Participante> findById(Long id);
}
