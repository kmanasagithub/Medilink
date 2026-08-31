package com.medilink.backend.Availability;

import com.medilink.backend.Availability.dto.AvailabilityRequest;
import com.medilink.backend.Availability.dto.AvailabilityResponse;
import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.Doctor.DoctorRepository;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService{

    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    // CREATE
    @Override
    @Transactional
    public AvailabilityResponse createAvailability(AvailabilityRequest request) {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor profile not found"
                        )
                );

        validateDoctor(doctor);

        validateTime(
                request.getStartTime(),
                request.getEndTime()
        );

        checkOverlap(
                doctor.getId(),
                request
        );

        DoctorAvailabilityEntity availability =
                DoctorAvailabilityEntity.builder()
                        .doctor(doctor)
                        .dayOfWeek(request.getDayOfWeek())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .consultationType(
                                request.getConsultationType()
                        )
                        .fee(request.getFee())
                        .active(true)
                        .build();

        DoctorAvailabilityEntity saved =
                availabilityRepository.save(availability);

        return convertToResponse(saved);
    }

    // GET MY AVAILABILITY
    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getMyAvailability() {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor profile not found"
                        )
                );

        return availabilityRepository
                .findByDoctorId(doctor.getId())
                .stream()
                .map(availability -> convertToResponse(availability))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getDoctorAvailability(UUID doctorId) {
        DoctorEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        if (doctor.getDoctorStatus() != DoctorStatus.ACTIVE) {
            throw new RuntimeException(
                    "Doctor is inactive"
            );
        }

        return availabilityRepository.findByDoctorId(doctorId)
                .stream()
                .map(availability -> convertToResponse(availability))
                .toList();
    }

    // UPDATE
    @Override
    @Transactional
    public AvailabilityResponse updateAvailability(UUID availabilityId, AvailabilityRequest request) {
        return null;
    }

    // DELETE / SOFT DELETE
    @Override
    @Transactional
    public void deleteAvailability(UUID availabilityId) {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor profile not found"
                        )
                );

        DoctorAvailabilityEntity availability =
                availabilityRepository.findById(availabilityId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Availability not found"
                                )
                        );

        validateOwnership(availability, doctor.getId());

        if (!availability.getActive()) {
            throw new RuntimeException(
                    "Availability is already inactive"
            );
        }

        availability.setActive(false);

    }

    // ACTIVATE
    @Override
    @Transactional
    public AvailabilityResponse activateAvailability(UUID availabilityId) {

        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found")
                );

        validateDoctor(doctor);

        DoctorAvailabilityEntity availability =
                availabilityRepository.findById(availabilityId)
                        .orElseThrow(() -> new RuntimeException("Availability not found"));


        validateOwnership(availability, doctor.getId());

        if (availability.getActive()) {
            throw new RuntimeException("Availability is already active");
        }

        // Check whether another active availability
        // now overlaps with this one.
        List<DoctorAvailabilityEntity> overlapping =
                availabilityRepository.findOverlappingAvailability(
                        doctor.getId(),
                        availability.getDayOfWeek(),
                        availability.getStartTime(),
                        availability.getEndTime()
                );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException(
                    "Cannot activate availability because it overlaps with another active availability"
            );
        }

        availability.setActive(true);
        return convertToResponse(availability);
    }

    // DEACTIVATE
    @Override
    @Transactional
    public AvailabilityResponse deactivateAvailability(UUID availabilityId) {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException(""));

        DoctorAvailabilityEntity availability =
                availabilityRepository.findById(availabilityId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Availability not found"
                                )
                        );

        validateOwnership(availability,doctor.getId());
        if (!availability.getActive()) {
            throw new RuntimeException(
                    "Availability is already inactive"
            );
        }

        availability.setActive(false);
        return convertToResponse(availability);
    }


    // =========================================================
    // VALIDATIONS
    // =========================================================

    private void validateDoctor(DoctorEntity doctor) {

        if (doctor.getVerificationStatus()
                != VerificationStatus.VERIFIED) {

            throw new RuntimeException(
                    "Doctor must be verified"
            );
        }

        if (doctor.getDoctorStatus()
                != DoctorStatus.ACTIVE) {

            throw new RuntimeException(
                    "Doctor must be active"
            );
        }
    }


    private void validateTime(
            LocalTime startTime,
            LocalTime endTime) {

        if (startTime == null || endTime == null) {
            throw new RuntimeException(
                    "Start time and end time are required"
            );
        }

        if (!startTime.isBefore(endTime)) {
            throw new RuntimeException(
                    "Start time must be before end time"
            );
        }
    }


    private void checkOverlap(
            UUID doctorId,
            AvailabilityRequest request) {

        List<DoctorAvailabilityEntity> overlapping =
                availabilityRepository.findOverlappingAvailability(
                        doctorId,
                        request.getDayOfWeek(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException(
                    "Availability overlaps with an existing availability"
            );
        }
    }


    private void checkOverlapForUpdate(
            UUID doctorId,
            UUID availabilityId,
            AvailabilityRequest request) {

        List<DoctorAvailabilityEntity> overlapping =
                availabilityRepository
                        .findOverlappingAvailabilityForUpdate(
                                doctorId,
                                request.getDayOfWeek(),
                                request.getStartTime(),
                                request.getEndTime(),
                                availabilityId
                        );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException(
                    "Availability overlaps with an existing availability"
            );
        }
    }


    private void validateOwnership(
            DoctorAvailabilityEntity availability,
            UUID doctorId) {

        if (!availability.getDoctor()
                .getId()
                .equals(doctorId)) {

            throw new RuntimeException(
                    "You are not authorized to modify this availability"
            );
        }
    }

    // =========================================================
    // MAPPER
    // =========================================================

    private AvailabilityResponse convertToResponse(
            DoctorAvailabilityEntity availability) {

        return AvailabilityResponse.builder()
                .availabilityId(availability.getId())
                .doctorId(availability.getDoctor().getId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .consultationType(
                        availability.getConsultationType()
                )
                .fee(availability.getFee())
                .active(availability.getActive())
                .build();
    }


    // =========================================================
    // CURRENT USER
    // =========================================================

    private UUID getLoggedInUserId() {

        /*
         * Replace this with Spring Security/JWT logic.
         */

        throw new UnsupportedOperationException(
                "Implement getLoggedInUserId() using Spring Security"
        );
    }


}
