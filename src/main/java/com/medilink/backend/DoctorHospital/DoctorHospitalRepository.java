package com.medilink.backend.DoctorHospital;

import com.medilink.backend.DoctorHospital.Enum.DoctorHospitalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorHospitalRepository extends JpaRepository<DoctorHospitalEntity, UUID> {
    boolean existsByDoctorIdAndHospitalId(UUID doctorId, UUID hospitalId);

    Optional<DoctorHospitalEntity> findByDoctorIdAndHospitalId(
            UUID doctorId,
            UUID hospitalId
    );

    List<DoctorHospitalEntity> findByHospitalId(UUID hospitalId);

    List<DoctorHospitalEntity> findByHospitalIdAndDepartmentIgnoreCase(
            UUID hospitalId,
            String department
    );

    List<DoctorHospitalEntity> findByDoctorId(UUID doctorId);

    List<DoctorHospitalEntity> findByHospitalIdAndStatus(
            UUID hospitalId,
            DoctorHospitalStatus status
    );

    List<DoctorHospitalEntity> findByDoctorIdAndStatus(
            UUID doctorId,
            DoctorHospitalStatus status
    );
}
