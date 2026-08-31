package com.medilink.backend.Appointment;

import com.medilink.backend.Appointment.Enum.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID>
{
    Page<AppointmentEntity> findByPatientId(UUID patientId, Pageable pageable);

    Page<AppointmentEntity> findByDoctorId(UUID doctorId, Pageable pageable);

    @Query("""
        SELECT COUNT(a) > 0
        FROM AppointmentEntity a
        WHERE a.doctor.id = :doctorId
          AND a.appointmentDate = :appointmentDate
          AND a.startTime < :endTime
          AND a.endTime > :startTime
          AND a.appointmentStatus IN :activeStatuses
    """)
    boolean existsOverlappingAppointment(
            @Param("doctorId") UUID doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("activeStatuses")
            Collection<AppointmentStatus> activeStatuses
    );
}
