package com.medilink.backend.Availability;

import com.medilink.backend.Availability.Enum.ConsultationType;
import com.medilink.backend.Doctor.DoctorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_availability")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id",nullable = false)
    private DoctorEntity doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationType consultationType;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal fee;

    @Column(nullable = false)
    private Boolean active;
}
