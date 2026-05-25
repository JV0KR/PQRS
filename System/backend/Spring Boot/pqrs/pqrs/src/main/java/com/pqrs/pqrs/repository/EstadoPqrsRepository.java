package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.EstadoPqrs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoPqrsRepository extends JpaRepository<EstadoPqrs, Long> {
    Optional<EstadoPqrs> findByNombre(String nombre);
}
