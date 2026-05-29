package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.Pqrs;
import com.pqrs.pqrs.entity.PqrsAnexo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PqrsAnexoRepository extends JpaRepository<PqrsAnexo, Long> {
    Optional<PqrsAnexo> findByPqrs(Pqrs pqrs);
}
