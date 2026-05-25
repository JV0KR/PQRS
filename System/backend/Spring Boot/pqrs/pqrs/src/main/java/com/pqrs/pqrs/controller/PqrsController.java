package com.pqrs.pqrs.controller;

import com.pqrs.pqrs.dto.PqrsResponseDTO;
import com.pqrs.pqrs.dto.RadicarPqrsDTO;
import com.pqrs.pqrs.dto.RadicacionPublicaDTO;
import com.pqrs.pqrs.service.PqrsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pqrs")
@CrossOrigin("*")
public class PqrsController {

    private final PqrsService service;

    public PqrsController(PqrsService service) {
        this.service = service;
    }

    @PostMapping("/radicar-publico")
    public ResponseEntity<?> radicarPublico(@Valid @RequestBody RadicacionPublicaDTO dto) {
        try {
            PqrsResponseDTO response = service.radicarPublico(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/radicar")
    public ResponseEntity<?> radicar(@Valid @RequestBody RadicarPqrsDTO dto) {
        try {
            PqrsResponseDTO response = service.radicar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/mis-radicados/{idCliente}")
    public ResponseEntity<List<PqrsResponseDTO>> consultarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(service.consultarPorCliente(idCliente));
    }

    @GetMapping
    public ResponseEntity<List<PqrsResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }
}
