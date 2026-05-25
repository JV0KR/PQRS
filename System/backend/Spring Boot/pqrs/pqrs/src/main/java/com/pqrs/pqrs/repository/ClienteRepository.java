package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.Cliente;
import com.pqrs.pqrs.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Cliente> findByCorreo(String correo);
    Optional<Cliente> findByUsuario(Usuario usuario);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByCorreo(String correo);
}
