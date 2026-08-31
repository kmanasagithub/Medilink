package com.medilink.backend.Availability;

import com.medilink.backend.Availability.dto.AvailabilityRequest;
import com.medilink.backend.Availability.dto.AvailabilityResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface AvailabilityService {

    AvailabilityResponse createAvailability(@Valid AvailabilityRequest request);

    List<AvailabilityResponse> getMyAvailability();

    List<AvailabilityResponse> getDoctorAvailability(
            UUID doctorId
    );

    AvailabilityResponse updateAvailability(
            UUID availabilityId,
            AvailabilityRequest request
    );

    void deleteAvailability(
            UUID availabilityId
    );

    AvailabilityResponse activateAvailability(
            UUID availabilityId
    );

    AvailabilityResponse deactivateAvailability(
            UUID availabilityId
    );
}
