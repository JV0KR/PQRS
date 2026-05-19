package com.pqrs.pqrs.service;

import com.pqrs.pqrs.entity.Pqrs;
import com.pqrs.pqrs.repository.PqrsRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PqrsService {

    private final PqrsRepository repository;

    public PqrsService(PqrsRepository repository) {
        this.repository = repository;
    }

    public List<Pqrs> listar() {
        return repository.findAll();
    }

    public Pqrs guardar(Pqrs pqrs) {

        pqrs.setFechaRadicado(
                LocalDateTime.now()
        );

        pqrs.setNumeroRadicado(
                "PQRS-" + System.currentTimeMillis()
        );

        return repository.save(pqrs);
    }
}