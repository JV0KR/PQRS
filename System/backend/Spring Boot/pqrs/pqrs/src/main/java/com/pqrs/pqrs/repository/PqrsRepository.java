package com.pqrs.pqrs.repository;

import com.pqrs.pqrs.entity.Pqrs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PqrsRepository
        extends JpaRepository<Pqrs, Long> {
}