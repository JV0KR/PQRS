package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.Gestor;
import com.pqrs.pqrs.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GestorRepository extends JpaRepository<Gestor, Long> {
    Optional<Gestor> findByUsuario(Usuario usuario);
    Optional<Gestor> findByCorreo(String correo);
}
