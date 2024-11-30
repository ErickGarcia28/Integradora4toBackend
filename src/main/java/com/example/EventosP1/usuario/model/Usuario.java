package com.example.EventosP1.usuario.model;

import com.example.EventosP1.evento.model.Evento;
import com.example.EventosP1.participante.model.Participante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(50)", nullable = false)
    private String nombre;

    @Column(name = "apellido", columnDefinition = "VARCHAR(80)", nullable = false)
    private String apellido;

    @Column(name = "correo_electronico", columnDefinition = "VARCHAR(74)", nullable = false, unique = true)
    private String correoElectronico;

    @Column(name = "telefono", columnDefinition = "BIGINT", nullable = false)
    private long telefono;

    @Column(name = "contrasena", columnDefinition = "VARCHAR(255)", nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", columnDefinition = "VARCHAR(50)", nullable = false)
    private Rol rol;


    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    @Column(name = "code", columnDefinition = "VARCHAR(6)")
    private String code;

    @Column(name = "code_expiration", columnDefinition = "TIMESTAMP")
    private LocalDateTime codeExpiration;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Evento> eventos;

    // Constructores
    public Usuario() {}

    public Usuario(String nombre, String apellido, String correoElectronico, long telefono, String contrasena, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.status = true;
    }

    public Usuario(Long id, String nombre, String apellido, String correoElectronico, int telefono, boolean status) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.status = true;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

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

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getCodeExpiration() {
        return codeExpiration;
    }

    public void setCodeExpiration(LocalDateTime codeExpiration) {
        this.codeExpiration = codeExpiration;
    }
}
