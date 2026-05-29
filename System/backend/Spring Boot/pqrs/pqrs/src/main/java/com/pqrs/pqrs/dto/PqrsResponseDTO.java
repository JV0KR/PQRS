package com.pqrs.pqrs.dto;

import java.time.LocalDateTime;

public class PqrsResponseDTO {
    private Long id;
    private String numeroRadicado;
    private LocalDateTime fechaRadicado;
    private String comentarios;
    private String tipoRadicado;
    private String estado;
    private String nombreCliente;
    private String correoCliente;
    private String justificacionEstado;
    private Boolean tieneAnexo;
    private Long idAnexo;

    public PqrsResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroRadicado() { return numeroRadicado; }
    public void setNumeroRadicado(String numeroRadicado) { this.numeroRadicado = numeroRadicado; }
    public LocalDateTime getFechaRadicado() { return fechaRadicado; }
    public void setFechaRadicado(LocalDateTime fechaRadicado) { this.fechaRadicado = fechaRadicado; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public String getTipoRadicado() { return tipoRadicado; }
    public void setTipoRadicado(String tipoRadicado) { this.tipoRadicado = tipoRadicado; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
    public String getJustificacionEstado() { return justificacionEstado; }
    public void setJustificacionEstado(String justificacionEstado) { this.justificacionEstado = justificacionEstado; }
    public Boolean getTieneAnexo() { return tieneAnexo; }
    public void setTieneAnexo(Boolean tieneAnexo) { this.tieneAnexo = tieneAnexo; }
    public Long getIdAnexo() { return idAnexo; }
    public void setIdAnexo(Long idAnexo) { this.idAnexo = idAnexo; }
}
