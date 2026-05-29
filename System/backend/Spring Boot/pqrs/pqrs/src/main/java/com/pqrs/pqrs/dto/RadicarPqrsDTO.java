package com.pqrs.pqrs.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class RadicarPqrsDTO {
    @NotNull
    private Long idCliente;

    @NotNull
    private Long idTipoRadicado;

    @NotBlank
    private String comentarios;
    private MultipartFile archivo;

    public RadicarPqrsDTO() {}

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public Long getIdTipoRadicado() { return idTipoRadicado; }
    public void setIdTipoRadicado(Long idTipoRadicado) { this.idTipoRadicado = idTipoRadicado; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public MultipartFile getArchivo() { return archivo; }
    public void setArchivo(MultipartFile archivo) { this.archivo = archivo; }
}
