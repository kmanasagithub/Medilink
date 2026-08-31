package com.medilink.backend.Appointment;

import com.medilink.backend.Appointment.Enum.AppointmentStatus;
import com.medilink.backend.Appointment.Enum.PaymentStatus;
import com.medilink.backend.Availability.Enum.ConsultationType;
import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.Patient.PatientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_id",nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doctor_id",nullable = false)
    private DoctorEntity doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal base_fee;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal final_amount;

    private String bookingReference;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
