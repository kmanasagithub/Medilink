package com.medilink.backend.DoctorHospital;

import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.DoctorHospital.Enum.DoctorHospitalStatus;
import com.medilink.backend.Hospital.HospitalEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="doctor_hospital",uniqueConstraints = {
        @UniqueConstraint(name = "uk_doctor_hospital",columnNames = {"doctor_id","hospital_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorHospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id",nullable = false)
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id",nullable = false)
    private HospitalEntity hospital;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorHospitalStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;
}
