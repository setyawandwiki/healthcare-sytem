package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findByNameContainingIgnoreCase(String name);
}
