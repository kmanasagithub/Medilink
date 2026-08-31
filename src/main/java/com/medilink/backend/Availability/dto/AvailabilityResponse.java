package com.medilink.backend.Availability.dto;

import com.medilink.backend.Availability.Enum.ConsultationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {

    private UUID availabilityId;

    private UUID doctorId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private ConsultationType consultationType;

    private BigDecimal fee;

    private Boolean active;
}
