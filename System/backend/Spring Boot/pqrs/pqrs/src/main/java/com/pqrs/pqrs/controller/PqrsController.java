package com.pqrs.pqrs.controller;

import com.pqrs.pqrs.entity.Pqrs;
import com.pqrs.pqrs.service.PqrsService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pqrs")
@CrossOrigin("*")
public class PqrsController {

    private final PqrsService service;

    public PqrsController(PqrsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pqrs> listar() {
        return service.listar();
    }

    @PostMapping
    public Pqrs guardar(
            @RequestBody Pqrs pqrs
    ) {
        return service.guardar(pqrs);
    }
}