package com.medilink.backend.Appointment.dto;

import com.medilink.backend.Availability.Enum.ConsultationType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AppointmentRequest {

    @NotNull
    private UUID doctorId;

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private ConsultationType consultationType;
}

