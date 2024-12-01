package com.example.EventosP1.usuario.model;

import com.example.EventosP1.evento.model.Evento;
import jakarta.validation.constraints.*;

import java.util.List;

public class UsuarioDTO {

    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "El ID no puede ser nulo.")
    private Long id;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede estar vacío.")
    @Size(max = 50, groups = {Register.class, Modify.class}, message = "El nombre no puede exceder los 50 caracteres.")
    private String nombre;

    @NotBlank(groups = {Register.class, Modify.class}, message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 80, groups = {Register.class, Modify.class}, message = "Los apellidos no pueden exceder los 80 caracteres.")
    private String apellido;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El correo electrónico no puede estar vacío.")
    @Email(groups = {Register.class, Modify.class}, message = "El correo electrónico debe ser válido.")
    @Size(max = 74, groups = {Register.class, Modify.class}, message = "El correo electrónico no puede exceder los 74 caracteres.")
    private String correoElectronico;

    @NotNull(groups = {Register.class, Modify.class}, message = "El teléfono no puede ser nulo.")
    @Min(value = 1000000000, groups = {Register.class, Modify.class}, message = "El teléfono debe tener exactamente 10 dígitos.")
    @Max(value = 9999999999L, groups = {Register.class, Modify.class}, message = "El teléfono debe tener exactamente 10 dígitos.")
    private long telefono;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La contraseña no puede estar vacía.")
    @Size(max = 255, groups = {Register.class, Modify.class}, message = "La contraseña no puede exceder los 255 caracteres.")
    private String contrasena;

    @NotNull(groups = {Register.class, Modify.class, ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Boolean status;

    private String code;


    public UsuarioDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }



    // Interfaces para grupos de validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
    public interface FindByEmail {}
    public interface VerifyCode {}

}
