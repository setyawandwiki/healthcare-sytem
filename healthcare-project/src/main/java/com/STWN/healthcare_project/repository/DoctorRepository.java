package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByHospitalId(Long hospitalId);
    Optional<Doctor> findByUserId(Long userId);
    @Query(value = "SELECT * FROM doctor WHERE " +
            "LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(bio) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    Page<Doctor> searchDoctor(@Param("keyword") String keyword, Pageable pageable);
}
