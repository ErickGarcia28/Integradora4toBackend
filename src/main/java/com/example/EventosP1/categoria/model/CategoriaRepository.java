package com.example.EventosP1.categoria.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Override
    Optional<Categoria> findById(Long id);
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findAllByStatusIsTrue();
}
