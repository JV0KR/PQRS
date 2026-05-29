package com.pqrs.pqrs.dto;

public class CambiarEstadoDTO {

    private Long idPqrs;
    private Long idEstado;
    private String justificacion;

    public CambiarEstadoDTO() {}

    public Long getIdPqrs() { return idPqrs; }
    public void setIdPqrs(Long idPqrs) { this.idPqrs = idPqrs; }
    public Long getIdEstado() { return idEstado; }
    public void setIdEstado(Long idEstado) { this.idEstado = idEstado; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
}
