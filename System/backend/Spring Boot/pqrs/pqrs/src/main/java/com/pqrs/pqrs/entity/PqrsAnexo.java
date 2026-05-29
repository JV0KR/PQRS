package com.pqrs.pqrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pqrs_anexo")
public class PqrsAnexo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anexo")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_pqrs", nullable = false, unique = true)
    private Pqrs pqrs;

    @Column(name = "nombre_archivo", nullable = false, length = 200)
    private String nombreArchivo;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "fecha_carga", nullable = false, updatable = false)
    private LocalDateTime fechaCarga;

    @PrePersist
    protected void onCreate() {
        this.fechaCarga = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pqrs getPqrs() { return pqrs; }
    public void setPqrs(Pqrs pqrs) { this.pqrs = pqrs; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Long getTamanoBytes() { return tamanoBytes; }
    public void setTamanoBytes(Long tamanoBytes) { this.tamanoBytes = tamanoBytes; }
    public LocalDateTime getFechaCarga() { return fechaCarga; }
    public void setFechaCarga(LocalDateTime fechaCarga) { this.fechaCarga = fechaCarga; }
}
