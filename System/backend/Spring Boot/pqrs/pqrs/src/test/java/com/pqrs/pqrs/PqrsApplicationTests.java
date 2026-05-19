package com.pqrs.pqrs;

import com.pqrs.pqrs.entity.Pqrs;
import com.pqrs.pqrs.repository.PqrsRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class PqrsApplicationTests {

    @Autowired
    private PqrsRepository repository;

    @Test
    void contextLoads() {

    }

    @Test
    void guardarPqrsTest() {

        Pqrs pqrs = new Pqrs();

        pqrs.setComentarios("Prueba de comentarios");
        pqrs.setNumeroRadicado("PQRS-TEST");
        pqrs.setFechaRadicado(LocalDateTime.now());

        Pqrs resultado = repository.save(pqrs);

        Assertions.assertNotNull(resultado.getId());
    }

    @Test
    void listarPqrsTest() {

        List<Pqrs> lista = repository.findAll();

        Assertions.assertNotNull(lista);
    }
}