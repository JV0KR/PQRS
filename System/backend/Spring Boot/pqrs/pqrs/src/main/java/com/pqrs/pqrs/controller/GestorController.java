package com.pqrs.pqrs.controller;

import com.pqrs.pqrs.dto.CambiarEstadoDTO;
import com.pqrs.pqrs.dto.PqrsResponseDTO;
import com.pqrs.pqrs.entity.EstadoPqrs;
import com.pqrs.pqrs.repository.EstadoPqrsRepository;
import com.pqrs.pqrs.service.GestorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gestor")
@CrossOrigin("*")
public class GestorController {

    private final GestorService gestorService;
    private final EstadoPqrsRepository estadoPqrsRepository;

    public GestorController(GestorService gestorService, EstadoPqrsRepository estadoPqrsRepository) {
        this.gestorService = gestorService;
        this.estadoPqrsRepository = estadoPqrsRepository;
    }

    @GetMapping("/radicados")
    public ResponseEntity<List<PqrsResponseDTO>> listarTodas() {
        return ResponseEntity.ok(gestorService.listarTodas());
    }

    @PutMapping("/cambiar-estado")
    public ResponseEntity<?> cambiarEstado(@Valid @RequestBody CambiarEstadoDTO dto) {
        try {
            PqrsResponseDTO response = gestorService.cambiarEstado(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoPqrs>> listarEstados() {
        return ResponseEntity.ok(estadoPqrsRepository.findAll());
    }

    @GetMapping("/descargar-anexo/{idPqrs}")
    public ResponseEntity<Resource> descargarAnexo(@PathVariable Long idPqrs) {
        try {
            com.pqrs.pqrs.entity.PqrsAnexo anexo = gestorService.obtenerAnexo(idPqrs);
            Resource resource = gestorService.cargarArchivoComoRecurso(anexo.getRutaArchivo());
            
            String contentType = anexo.getMimeType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNombreArchivo() + "\"")
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/reporte-pdf")
    public ResponseEntity<byte[]> generarReportePdf() {
        byte[] pdf = gestorService.generarReportePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("reporte_pqrs.pdf").build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
