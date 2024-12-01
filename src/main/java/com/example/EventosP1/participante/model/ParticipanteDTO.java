package com.example.EventosP1.participante.model;

import jakarta.validation.constraints.*;

public class ParticipanteDTO {

    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "El ID no puede ser nulo.")
    private Long id;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede estar vacío.")
    @Size(max = 50, groups = {Register.class, Modify.class}, message = "El nombre no puede exceder los 50 caracteres.")
    private String nombre;

    @NotBlank(groups = {Register.class, Modify.class}, message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 80, groups = {Register.class, Modify.class}, message = "Los apellidos no pueden exceder los 80 caracteres.")
    private String apellido;

    @NotNull(groups = {Register.class, Modify.class}, message = "El teléfono no puede ser nulo.")
    @Min(value = 1000000000, groups = {Register.class, Modify.class}, message = "El teléfono debe tener al menos 10 dígitos.")
    @Max(value = 9999999999L, groups = {Register.class, Modify.class}, message = "El teléfono no puede exceder los 10 dígitos.")
    private Integer telefono;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El correo electrónico no puede estar vacío.")
    @Email(groups = {Register.class, Modify.class}, message = "El correo electrónico debe ser válido.")
    @Size(max = 74, groups = {Register.class, Modify.class}, message = "El correo electrónico no puede exceder los 74 caracteres.")
    private String correoElectronico;

    @NotNull(groups = {Register.class}, message = "El evento no puede ser nulo.")
    private Long eventoId; // Evento relacionado

    @NotBlank(groups = {Register.class, Modify.class}, message = "La dirección no puede estar vacía.")
    @Size(max = 250, groups = {Register.class, Modify.class}, message = "La dirección no puede exceder los 250 caracteres.")
    private String direccion;

    @NotNull(groups = {Register.class,  ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Boolean status;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getTelefono() { return telefono; }
    public void setTelefono(int telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    // Interfaces para validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
