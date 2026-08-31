package com.medilink.backend.Availability.dto;

import com.medilink.backend.Availability.Enum.ConsultationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailabilityRequest{

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Consultation type is required")
    private ConsultationType consultationType;

    @NotNull
    @DecimalMin(value="0.0",inclusive = true,
            message = "Fee must be greater than or equal to 0")
    private BigDecimal fee;
}
