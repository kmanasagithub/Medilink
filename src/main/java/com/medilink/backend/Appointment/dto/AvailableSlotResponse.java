package com.medilink.backend.Appointment.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AvailableSlotResponse {

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal fee;

    private boolean available;

}
