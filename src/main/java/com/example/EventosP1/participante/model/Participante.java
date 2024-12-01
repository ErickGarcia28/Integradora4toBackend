package com.example.EventosP1.participante.model;

import com.example.EventosP1.evento.model.Evento;
import jakarta.persistence.*;

@Entity
@Table(name = "participantes")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(50)", nullable = false)
    private String nombre;

    @Column(name = "apellido", columnDefinition = "VARCHAR(80)", nullable = false)
    private String apellido;

    @Column(name = "telefono", columnDefinition = "BIGINT", nullable = false)
    private long telefono;

    @Column(name = "correo_electronico", columnDefinition = "VARCHAR(74)", nullable = false, unique = true)
    private String correoElectronico;

    @ManyToOne()
    private Evento evento;

    @Column(name = "direccion", columnDefinition = "VARCHAR(250)", nullable = false)
    private String direccion;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    // Constructores
    public Participante() {}

    public Participante(String nombre, String apellido, long telefono, String correoElectronico, Evento evento, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.evento = evento;
        this.direccion = direccion;
        this.status = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
