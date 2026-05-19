package com.pqrs.pqrs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pqrs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pqrs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pqrs")
    private Long id;

    @Column(name = "numero_radicado")
    private String numeroRadicado;

    private String comentarios;

    @Column(name = "fecha_radicado")
    private LocalDateTime fechaRadicado;
}