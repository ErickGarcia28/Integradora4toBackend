package com.example.EventosP1.usuario.model;

import com.example.EventosP1.evento.model.Evento;
import jakarta.validation.constraints.*;

import java.util.List;
public class PerfilDTO {

    @NotNull(groups = {UpdateProfile.class}, message = "El ID no puede ser nulo.")
    private Long id;

    @NotBlank(groups = {UpdateProfile.class}, message = "El nombre no puede estar vacío.")
    @Size(max = 50, groups = {UpdateProfile.class}, message = "El nombre no puede exceder los 50 caracteres.")
    private String nombre;

    @NotBlank(groups = {UpdateProfile.class}, message = "El apellido no puede estar vacío.")
    @Size(max = 80, groups = {UpdateProfile.class}, message = "El apellido no puede exceder los 80 caracteres.")
    private String apellido;

    @NotBlank(groups = {UpdateProfile.class}, message = "El correo electrónico no puede estar vacío.")
    @Email(groups = {UpdateProfile.class}, message = "El correo electrónico debe ser válido.")
    @Size(max = 74, groups = {UpdateProfile.class}, message = "El correo electrónico no puede exceder los 74 caracteres.")
    private String correoElectronico;

    @NotNull(groups = {UpdateProfile.class}, message = "El teléfono no puede ser nulo.")
    @Min(value = 1000000000, groups = {UpdateProfile.class}, message = "El teléfono debe tener exactamente 10 digitos.")
    @Max(value = 9999999999L, groups = {UpdateProfile.class}, message = "El teléfono debe tener exactamente 10 digitos.")
    private long telefono;

    @NotNull(groups = {UpdateProfile.class}, message = "El estado no puede ser nulo.")
    private Boolean status;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // Grupo de validación para la actualización del perfil
    public interface UpdateProfile {}

    // Interfaces para grupos de validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
