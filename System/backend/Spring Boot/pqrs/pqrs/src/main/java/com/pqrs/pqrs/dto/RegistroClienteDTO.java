package com.pqrs.pqrs.dto;

import jakarta.validation.constraints.*;

public class RegistroClienteDTO {
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

    public RegistroClienteDTO() {}

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
}
