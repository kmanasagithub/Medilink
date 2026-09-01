package com.medilink.backend.Hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, UUID> {
    @Query("""
        SELECT h 
        FROM HospitalEntity h
        WHERE h.status =  com.medilink.backend.Hospital.Enum.HospitalStatus.ACTIVE
           AND (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%',:name,'%')))
           AND (:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%',:city,'%')))
           AND (:state IS NULL OR LOWER(h.state) LIKE LOWER(CONCAT('%',:state,'%')))
           AND (:country IS NULL OR LOWER(h.country) LIKE LOWER(CONCAT('%',:country,'%')))
    """)
    List<HospitalEntity> searchHospitals(
            @Param("name") String name,
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country
    );

    Optional<HospitalEntity> findByUserId(UUID userId);
}
