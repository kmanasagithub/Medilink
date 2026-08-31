package com.medilink.backend.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {
    boolean existsByUserId(UUID userId);

    Optional<PatientEntity> findByUserId(UUID userId);
}
