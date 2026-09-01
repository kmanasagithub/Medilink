package com.medilink.backend.Availability;

import com.medilink.backend.Availability.Enum.ConsultationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, UUID> {

    List<DoctorAvailabilityEntity> findByDoctorId(UUID doctorId);

    List<DoctorAvailabilityEntity> findByDoctorIdAndActiveTrue(UUID doctorId);

    List<DoctorAvailabilityEntity> findByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DayOfWeek dayOfWeek
    );

    List<DoctorAvailabilityEntity> findByDoctorIdAndDayOfWeekAndActiveTrue(
            UUID doctorId,
            DayOfWeek dayOfWeek
    );

    @Query("""
        SELECT a
        FROM DoctorAvailabilityEntity a
        WHERE a.doctor.id = :doctorId
        AND a.dayOfWeek = :dayOfWeek
        AND a.active = true
        AND a.startTime < :endTime
        AND a.endTime > :startTime
    """)
    List<DoctorAvailabilityEntity> findOverlappingAvailability(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
        SELECT a 
        FROM DoctorAvailabilityEntity a
        WHERE a.doctor.id = :doctorId
                AND a.dayOfWeek = :dayOfWeek
                AND a.active = true
                AND a.id <> :availabilityId
                AND a.startTime < :endTime
                AND a.endTime > :startTime
    """)
    List<DoctorAvailabilityEntity> findOverlappingAvailabilityForUpdate(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("availabilityId") UUID availabilityId
    );

    @Query("""
    SELECT a
    FROM DoctorAvailabilityEntity a
    WHERE a.doctor.id = :doctorId
      AND a.dayOfWeek = :dayOfWeek
      AND a.consultationType = :consultationType
      AND a.active = true
      AND a.startTime <= :startTime
      AND a.endTime >= :endTime
""")
    List<DoctorAvailabilityEntity> findMatchingAvailability(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("consultationType") ConsultationType consultationType,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

}
