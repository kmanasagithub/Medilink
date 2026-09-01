package com.medilink.backend.Hospital;

import com.medilink.backend.DoctorHospital.DoctorHospitalEntity;
import com.medilink.backend.Hospital.Enum.HospitalStatus;
import com.medilink.backend.User.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "hospital")
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            unique = true,
            nullable = false
    )
    private UserEntity user;

    @Column(nullable = false)
    private String hospitalName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalStatus status;

    /*
     * Hospital → Doctors
     */
    @Builder.Default
    @OneToMany(mappedBy = "hospital",fetch = FetchType.LAZY)
    private List<DoctorHospitalEntity> doctorHospitals = new ArrayList<>();
}
