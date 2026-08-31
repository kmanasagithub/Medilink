package com.medilink.backend.Doctor;

import com.medilink.backend.Appointment.AppointmentEntity;
import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.DoctorHospital.DoctorHospitalEntity;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "doctors")
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",unique = true, nullable = false)
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private Integer experienceYears;

    private String bio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorStatus doctorStatus;

    @OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    /*
     * Doctor → Hospitals
     */
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DoctorHospitalEntity> doctorHospitals = new ArrayList<>();


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
