package com.example.EventosP1.categoria.model;

import com.example.EventosP1.evento.model.Evento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(200)", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(300)", nullable = false)
    private String descripcion;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Evento> eventos;

    // Constructores
    public Categoria() {}

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.status = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public List<Evento> getEventos() { return eventos; }
    public void setEventos(List<Evento> eventos) { this.eventos = eventos; }
}
