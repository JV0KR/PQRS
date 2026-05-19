package com.pqrs.pqrs.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pqrs")
public class Pqrs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pqrs")
    private Long id;

    @Column(name = "numero_radicado")
    private String numeroRadicado;

    @Column(nullable = false)
    private String comentarios;

    @Column(name = "fecha_radicado")
    private LocalDateTime fechaRadicado;

    // Constructor vacío
    public Pqrs() {
    }

    // GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroRadicado() {
        return numeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public LocalDateTime getFechaRadicado() {
        return fechaRadicado;
    }

    public void setFechaRadicado(LocalDateTime fechaRadicado) {
        this.fechaRadicado = fechaRadicado;
    }
}