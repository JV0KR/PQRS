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

    @Column(name = "numero_radicado", nullable = false, unique = true, length = 30)
    private String numeroRadicado;

    @Column(name = "fecha_radicado", nullable = false)
    private LocalDateTime fechaRadicado;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "justificacion_estado", columnDefinition = "TEXT")
    private String justificacionEstado;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_tipo_radicado", nullable = false)
    private TipoRadicado tipoRadicado;

    @ManyToOne
    @JoinColumn(name = "id_estado_actual", nullable = false)
    private EstadoPqrs estadoActual;

    @ManyToOne
    @JoinColumn(name = "id_usuario_crea")
    private Usuario usuarioCrea;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Pqrs() {}

    @PrePersist
    public void prePersist() {
        this.fechaRadicado = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroRadicado() { return numeroRadicado; }
    public void setNumeroRadicado(String numeroRadicado) { this.numeroRadicado = numeroRadicado; }
    public LocalDateTime getFechaRadicado() { return fechaRadicado; }
    public void setFechaRadicado(LocalDateTime fechaRadicado) { this.fechaRadicado = fechaRadicado; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public String getJustificacionEstado() { return justificacionEstado; }
    public void setJustificacionEstado(String justificacionEstado) { this.justificacionEstado = justificacionEstado; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public TipoRadicado getTipoRadicado() { return tipoRadicado; }
    public void setTipoRadicado(TipoRadicado tipoRadicado) { this.tipoRadicado = tipoRadicado; }
    public EstadoPqrs getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoPqrs estadoActual) { this.estadoActual = estadoActual; }
    public Usuario getUsuarioCrea() { return usuarioCrea; }
    public void setUsuarioCrea(Usuario usuarioCrea) { this.usuarioCrea = usuarioCrea; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
