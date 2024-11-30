package com.example.EventosP1.evento.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;  // Importar LocalTime para la hora

public class EventoDTO {

    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "El ID no puede ser nulo.")
    private Long id;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede estar vacío.")
    @Size(max = 200, groups = {Register.class, Modify.class}, message = "El nombre no puede exceder los 200 caracteres.")
    private String nombre;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripcion no puede estar vacía.")
    @Size(max = 300, groups = {Register.class, Modify.class}, message = "La descripcion no puede exceder los 300 caracteres.")
    private String descripcion;

    @NotNull(groups = {Register.class, Modify.class}, message = "La fecha no puede ser nula.")
    private LocalDate fecha;

    @NotNull(groups = {Register.class, Modify.class}, message = "La hora no puede ser nula.")  // Validación para la hora
    private LocalTime hora;  // Hora del evento

    @NotBlank(groups = {Register.class, Modify.class}, message = "El lugar no puede estar vacío.")
    @Size(max = 250, groups = {Register.class, Modify.class}, message = "El lugar no puede exceder los 250 caracteres.")
    private String lugar;

    @NotNull(groups = {Register.class, Modify.class}, message = "La categoría no puede ser nula.")
    private long categoriaId;

    @NotNull(groups = {Register.class, Modify.class, ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Boolean status;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }  // Getter para la hora
    public void setHora(LocalTime hora) { this.hora = hora; }  // Setter para la hora

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(long categoria) { this.categoriaId = categoria; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }



    // Interfaces para grupos de validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
