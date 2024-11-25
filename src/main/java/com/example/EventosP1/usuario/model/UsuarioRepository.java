package com.example.EventosP1.usuario.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar usuarios por nombre ignorando mayúsculas y minúsculas
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    Optional<Usuario> findFirstByCorreoElectronico(String correoElectronico);


    Optional<Usuario> findFirstByCorreoElectronicoAndCode(String correoElectronico, String code);

}