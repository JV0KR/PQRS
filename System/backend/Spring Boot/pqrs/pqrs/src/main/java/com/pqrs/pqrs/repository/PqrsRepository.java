package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.Cliente;
import com.pqrs.pqrs.entity.Pqrs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PqrsRepository extends JpaRepository<Pqrs, Long> {
    List<Pqrs> findByClienteOrderByFechaRadicadoDesc(Cliente cliente);
}
