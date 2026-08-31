package com.medilink.backend.Appointment.dto;

import com.medilink.backend.Appointment.Enum.AppointmentStatus;
import com.medilink.backend.Appointment.Enum.PaymentStatus;
import com.medilink.backend.Availability.Enum.ConsultationType;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Patient.dto.PatientResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentResponse {

    private UUID appointmentId;

    private String bookingReference;

    private DoctorResponse doctor;

    private PatientResponseDto patient;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private ConsultationType consultationType;

    private BigDecimal baseFee;

    private BigDecimal finalAmount;

    private AppointmentStatus appointmentStatus;

    private PaymentStatus paymentStatus;

    private LocalDateTime confirmedAt;

    private LocalDateTime doctorJoinedAt;
}
