package com.backendapi.repository;

import com.backendapi.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GateRepository extends JpaRepository<Gate, Long> {
    List<Gate> findByActiveTrue();
}
