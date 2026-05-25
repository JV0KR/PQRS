package com.pqrs.pqrs.dto;

import jakarta.validation.constraints.*;

public class LoginDTO {
    @NotBlank
    @Email
    private String correo;

    @NotBlank
    private String password;

    public LoginDTO() {}

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
