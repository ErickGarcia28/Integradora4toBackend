package com.example.EventosP1.evento.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findAllByStatusIsTrue();
    List<Evento> findAllByStatusIsTrueOrderByFechaAsc();  // Ordenado por fecha ascendente
    Optional<Evento> findByNombre(String nombre);
    Optional<Evento> findById(Long id);

    List<Evento> findAllByUsuarioId(Long usuarioId);
    List<Evento> findAllByUsuarioIdAndStatusIsTrue(Long usuarioId);

}
