package com.example.EventosP1.categoria.model;

import com.example.EventosP1.evento.model.Evento;
import jakarta.validation.constraints.*;

import java.util.List;

public class CategoriaDTO {

    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "El ID no puede ser nulo.")
    private Long id;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede estar vacío.")
    @Size(max = 200, groups = {Register.class, Modify.class}, message = "El nombre no puede exceder los 200 caracteres.")
    private String nombre;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripción no puede estar vacía.")
    @Size(max = 300, groups = {Register.class, Modify.class}, message = "La descripción no puede exceder los 300 caracteres.")
    private String descripcion;

    @NotNull(groups = {Register.class, Modify.class, ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Boolean status;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    // Interfaces para grupos de validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
