package com.medilink.backend.Availability;

import com.medilink.backend.Availability.dto.AvailabilityRequest;
import com.medilink.backend.Availability.dto.AvailabilityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // DOCTOR CREATES AVAILABILITY
    @PostMapping("/doctors/me/availability")
    public ResponseEntity<AvailabilityResponse> createAvailability(@RequestBody @Valid AvailabilityRequest
                                                                   request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                availabilityService.createAvailability(request)
        );
    }

    // DOCTOR GETS OWN AVAILABILITY
    @GetMapping("/doctors/me/availability")
    public ResponseEntity<List<AvailabilityResponse>> getMyAvailability() {
        return ResponseEntity.ok(availabilityService.getMyAvailability());
    }

    // PUBLIC - GET DOCTOR AVAILABILITY
    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity<List<AvailabilityResponse>> getDoctorAvailability(
            @PathVariable UUID doctorId) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(doctorId));
    }

    // UPDATE AVAILABILITY
    @PutMapping("/doctors/me/availability/{availabilityId}")
    public ResponseEntity<AvailabilityResponse> updateAvailability(
            @PathVariable UUID availabilityId,
            @RequestBody @Valid AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.updateAvailability(
                        availabilityId, request )
        );
    }

    // DELETE / SOFT DELETE
    @DeleteMapping("/doctors/me/availability/{availabilityId}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable UUID availabilityId) {
        availabilityService.deleteAvailability(availabilityId);
        return ResponseEntity.noContent().build();
    }

    // ACTIVATE
    @PatchMapping("/doctors/me/availability/{availabilityId}/activate")
    public ResponseEntity<AvailabilityResponse> activateAvailability(
            @PathVariable UUID availabilityId) {
        return ResponseEntity.ok(
                availabilityService.activateAvailability(availabilityId)
        );
    }

    // DEACTIVATE
    @DeleteMapping("/doctors/me/availability/{availabilityId}/deactivate")
    public ResponseEntity<AvailabilityResponse> deactivateAvailability(
            @PathVariable UUID availabilityId) {
        return ResponseEntity.ok(
                availabilityService.deactivateAvailability(availabilityId)
        );
    }

}
