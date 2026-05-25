package com.pqrs.pqrs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_pqrs")
public class EstadoPqrs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_pqrs")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre;

    private Integer orden;

    @Column(nullable = false)
    private Boolean activo = true;

    public EstadoPqrs() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
