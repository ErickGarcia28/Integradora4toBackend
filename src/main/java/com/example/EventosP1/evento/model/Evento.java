package com.example.EventosP1.evento.model;

import com.example.EventosP1.categoria.model.Categoria;
import com.example.EventosP1.participante.model.Participante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(200)", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(300)", nullable = false)
    private String descripcion;

    @Column(name = "fecha", columnDefinition = "DATE", nullable = false)
    private LocalDate fecha;

    @Column(name = "lugar", columnDefinition = "VARCHAR(250)", nullable = false)
    private String lugar;

    @ManyToOne()
    private Categoria categoria;

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private List<Participante> participantes;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    // Constructores
    public Evento() {}

    public Evento(String nombre, LocalDate fecha, String lugar, Categoria categoria, String descripcion) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.categoria = categoria;
        this.status = true;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public List<Participante> getParticipantes() { return participantes; }
    public void setParticipantes(List<Participante> participantes) { this.participantes = participantes; }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
