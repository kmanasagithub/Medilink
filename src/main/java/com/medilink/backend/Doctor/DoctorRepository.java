package com.medilink.backend.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {

    boolean existsByUserId(UUID userId);

    Optional<DoctorEntity> findByUserId(UUID userId);

}
