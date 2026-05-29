package com.pqrs.pqrs.dto;

public class LoginResponseDTO {

    private Long id;
    private String nombresCompletos;
    private String correo;
    private String perfil;

    public LoginResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombresCompletos() { return nombresCompletos; }
    public void setNombresCompletos(String nombresCompletos) { this.nombresCompletos = nombresCompletos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}
