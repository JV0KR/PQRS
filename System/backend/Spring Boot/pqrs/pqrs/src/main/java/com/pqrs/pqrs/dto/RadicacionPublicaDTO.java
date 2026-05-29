package com.pqrs.pqrs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class RadicacionPublicaDTO {
    @NotNull
    private Long idTipoIdentificacion;
    @NotBlank
    private String numeroIdentificacion;
    @NotBlank
    private String nombresCompletos;
    @NotBlank
    @Email
    private String correo;
    private String telefonoMovil;
    
    @NotNull
    private Long idTipoRadicado;
    @NotBlank
    private String comentarios;
    private MultipartFile archivo;

    public RadicacionPublicaDTO() {}

    public Long getIdTipoIdentificacion() { return idTipoIdentificacion; }
    public void setIdTipoIdentificacion(Long idTipoIdentificacion) { this.idTipoIdentificacion = idTipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    public String getNombresCompletos() { return nombresCompletos; }
    public void setNombresCompletos(String nombresCompletos) { this.nombresCompletos = nombresCompletos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefonoMovil() { return telefonoMovil; }
    public void setTelefonoMovil(String telefonoMovil) { this.telefonoMovil = telefonoMovil; }
    public Long getIdTipoRadicado() { return idTipoRadicado; }
    public void setIdTipoRadicado(Long idTipoRadicado) { this.idTipoRadicado = idTipoRadicado; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public MultipartFile getArchivo() { return archivo; }
    public void setArchivo(MultipartFile archivo) { this.archivo = archivo; }
}
