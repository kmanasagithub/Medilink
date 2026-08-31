package com.medilink.backend.Patient;

import com.medilink.backend.Appointment.AppointmentEntity;
import com.medilink.backend.Patient.Enum.BloodGroup;
import com.medilink.backend.Patient.Enum.Gender;
import com.medilink.backend.Patient.Enum.PatientStatus;
import com.medilink.backend.Patient.dto.Address;
import com.medilink.backend.User.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patients")
@Entity
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private UserEntity user;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String allergies;

    private String emergencyContact;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientStatus patientStatus;

    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
