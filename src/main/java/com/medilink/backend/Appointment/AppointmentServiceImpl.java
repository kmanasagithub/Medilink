package com.medilink.backend.Appointment;

import com.medilink.backend.Appointment.Enum.AppointmentStatus;
import com.medilink.backend.Appointment.Enum.PaymentStatus;
import com.medilink.backend.Appointment.dto.AppointmentRequest;
import com.medilink.backend.Appointment.dto.AppointmentResponse;
import com.medilink.backend.Appointment.dto.AvailableSlotResponse;
import com.medilink.backend.Availability.AvailabilityRepository;
import com.medilink.backend.Availability.DoctorAvailabilityEntity;
import com.medilink.backend.Availability.Enum.ConsultationType;
import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.Doctor.DoctorRepository;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Patient.PatientEntity;
import com.medilink.backend.Patient.PatientRepository;
import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AvailabilityRepository availabilityRepository;

    private static final int SLOT_DURATION_MINUTES = 30;


    private static final List<AppointmentStatus> ACTIVE_APPOINTMENT_STATUSES =
            List.of(
                    AppointmentStatus.PENDING_PAYMENT,
                    AppointmentStatus.CONFIRMED,
                    AppointmentStatus.WAITING_FOR_DOCTOR,
                    AppointmentStatus.IN_PROGRESS
            );

    // AVAILABLE SLOTS
    @Override
    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlots(UUID doctorId, LocalDate date, ConsultationType consultationType) {
        DoctorEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        validateDoctorForBooking(doctor);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        List<DoctorAvailabilityEntity> availabilities = availabilityRepository
                        .findByDoctorIdAndDayOfWeekAndActiveTrue(doctorId, dayOfWeek)
                        .stream()
                        .filter(a ->
                                a.getConsultationType() == consultationType
                        )
                        .toList();

        if (availabilities.isEmpty()) {
            return List.of();
        }

        List<AvailableSlotResponse> slots = new java.util.ArrayList<>();

        for (DoctorAvailabilityEntity availability : availabilities) {

            LocalTime slotStart = availability.getStartTime();
            while (slotStart.plusMinutes(SLOT_DURATION_MINUTES).compareTo(availability.getEndTime()) <= 0) {

                LocalTime slotEnd = slotStart.plusMinutes(SLOT_DURATION_MINUTES);
                boolean booked = appointmentRepository.existsOverlappingAppointment(
                                        doctorId,
                                        date,
                                        slotStart,
                                        slotEnd,
                                        ACTIVE_APPOINTMENT_STATUSES
                                );

                slots.add(new AvailableSlotResponse(
                                slotStart,
                                slotEnd,
                                availability.getFee(),
                                !booked)
                );

                slotStart = slotEnd;
            }
        }

        return slots;

    }

    // CREATE APPOINTMENT
    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request){
        UUID patientUserId = getLoggedInUserId();

        PatientEntity patient = patientRepository.findByUserId(patientUserId)
                        .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        DoctorEntity doctor = doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));

        validateDoctorForBooking(doctor);
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Appointment date cannot be in the past");
        }

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusMinutes(SLOT_DURATION_MINUTES);
        DayOfWeek dayOfWeek = request.getAppointmentDate().getDayOfWeek();

        /*
         * Find availability that covers the requested slot.
         */
        List<DoctorAvailabilityEntity> matchingAvailability = availabilityRepository.findMatchingAvailability(doctor.getId(),
                dayOfWeek,request.getConsultationType(),startTime,endTime);
        if(matchingAvailability.isEmpty()) {
            throw new RuntimeException("Doctor is not available for the selected time");
        }

        /*
         * Use the fee from the matching availability.
         */
        DoctorAvailabilityEntity availability = matchingAvailability.get(0);
        validateTime(startTime, endTime);

        /*Prevent double booking*/
        boolean alreadyBooked = appointmentRepository.existsOverlappingAppointment(doctor.getId(),
                        request.getAppointmentDate(), startTime, endTime, ACTIVE_APPOINTMENT_STATUSES
                );

        if (alreadyBooked) {
            throw new RuntimeException("Selected appointment slot is already booked");
        }

        BigDecimal baseFee = availability.getFee();

        /*
         * For now:
         * finalAmount = baseFee
         * Later you can add discounts, taxes, coupons, etc.
         */

        BigDecimal finalAmount = baseFee;

        String bookingReference = generateBookingReference();

        AppointmentEntity appointment = AppointmentEntity.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .startTime(startTime)
                .endTime(endTime)
                .consultationType(request.getConsultationType())
                .baseFee(baseFee)
                .finalAmount(finalAmount)
                .bookingReference(bookingReference)
                .appointmentStatus(AppointmentStatus.PENDING_PAYMENT)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        AppointmentEntity saved =
                appointmentRepository.save(appointment);

        return convertToResponse(saved);
    }

    // GET APPOINTMENTS
    @Override
    @Transactional
    public AppointmentResponse getAppointment(UUID appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        UUID currentUserId = getLoggedInUserId();
        boolean isPatient = appointment.getPatient().getUser().getId()
                .equals(currentUserId);
        boolean isDoctor = appointment.getDoctor().getUser().getId()
                .equals(currentUserId);

        if(!isPatient && !isDoctor){
            throw new RuntimeException("You are not authorized to view this appointment");
        }

        return convertToResponse(appointment);
    }

    // PATIENT APPOINTMENTS
    @Override
    @Transactional
    public Page<AppointmentResponse> getMyAppointments(Pageable pageable) {

        UUID userId = getLoggedInUserId();

        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return appointmentRepository.findByPatientId(patient.getId(),pageable)
                .map(appointment -> convertToResponse(appointment));

    }

    // DOCTOR'S APPOINTMENTS
    @Override
    @Transactional
    public Page<AppointmentResponse> getDoctorAppointments(Pageable pageable) {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        return appointmentRepository.findByDoctorId(doctor.getId(),pageable)
                .map(appointment -> convertToResponse(appointment));
    }

    // CANCEL APPOINTMENT
    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId, String reason) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                        .orElseThrow(() -> new RuntimeException("Appointment not found"));

        UUID userId = getLoggedInUserId();

        boolean isPatient = appointment.getPatient().getUser().getId()
                        .equals(userId);
        boolean isDoctor = appointment.getDoctor().getUser().getId()
                        .equals(userId);

        if (!isPatient && !isDoctor) {
            throw new RuntimeException("You are not authorized to cancel this appointment");
        }

        if(appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment is already cancelled");
        }

        if(appointment.getAppointmentStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Completed appointment cannot be cancelled");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);

        /*
         * If payment was successful,
         * refund logic will be handled here later.
         */
        if (appointment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            appointment.setPaymentStatus(PaymentStatus.REFUND_PENDING);
        }

        return convertToResponse(appointment);
    }

    // DOCTOR
    @Override
    @Transactional
    public AppointmentResponse joinAppointment(UUID appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        UUID userId = getLoggedInUserId();
        boolean isDoctor = appointment.getDoctor().getUser().getId().equals(userId);

        if(!isDoctor) {
            throw new RuntimeException("Only the assigned doctor can join this appointment");
        }

        if (appointment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment is not completed");
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.CONFIRMED) {
            throw new RuntimeException("Appointment is not ready for consultation");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledStart = LocalDateTime.of(appointment.getAppointmentDate(),
                        appointment.getStartTime());

        /*
         * Doctor can join 5 minutes before
         * scheduled start.
         */
        if(now.isBefore(scheduledStart.minusMinutes(5))) {
            throw new RuntimeException("Doctor cannot join yet");
        }

        appointment.setDoctorJoinedAt(now);
        appointment.setConsultationStartedAt(now);
        appointment.setAppointmentStatus(AppointmentStatus.IN_PROGRESS);

        return convertToResponse(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId).orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        UUID userId = getLoggedInUserId();

        boolean isDoctor = appointment.getDoctor().getUser().getId()
                .equals(userId);

        if(!isDoctor) {
            throw new RuntimeException("Only the assigned doctor can complete the appointment");
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new RuntimeException("Appointment is not in progress");
        }

        appointment.setConsultationEndedAt(LocalDateTime.now());
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);

        return convertToResponse(appointment);
    }
    // -------------------------------------------------------------------
    // VALIDATION
    private void validateDoctorForBooking(DoctorEntity doctor) {
        if (doctor.getDoctorStatus() != DoctorStatus.ACTIVE) {
            throw new RuntimeException("Doctor is inactive");
        }

        if (doctor.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new RuntimeException("Doctor is not verified");
        }
    }

    private void validateTime(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new RuntimeException("Start time must be before end time");
        }
    }

    // BOOKING REFERENCE
    private String generateBookingReference() {
        return "MED-" +
                LocalDate.now().toString().replace("-", "") +
                "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // MAPPER
    private AppointmentResponse convertToResponse(AppointmentEntity appointment) {

        DoctorResponse doctorResponse = convertToDoctorResponse(appointment.getDoctor());
        PatientResponseDto patientResponse = convertToPatientResponse(appointment.getPatient());

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getBookingReference(),
                doctorResponse,
                patientResponse,
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getConsultationType(),
                appointment.getBaseFee(),
                appointment.getFinalAmount(),
                appointment.getAppointmentStatus(),
                appointment.getPaymentStatus(),
                appointment.getConfirmedAt(),
                appointment.getDoctorJoinedAt()
        );

    }

    private DoctorResponse convertToDoctorResponse(DoctorEntity doctor) {
        // convert DoctorEntity to DoctorResponse
        UserEntity user = doctor.getUser();

        UserResponse userResponse =
                UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole())
                        .build();

        return DoctorResponse.builder()
                .id(doctor.getId())
                .user(userResponse)
                .licenseNumber(doctor.getLicenseNumber())
                .qualification(doctor.getQualification())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .verificationStatus(doctor.getVerificationStatus())
                .doctorStatus(doctor.getDoctorStatus())
                .build();
    }

    private PatientResponseDto convertToPatientResponse(PatientEntity patient) {

        UserEntity user = patient.getUser();

        UserResponse userResponse =
                UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole())
                        .build();

        return PatientResponseDto.builder()
                .id(patient.getId())
                .user(userResponse)
                .build();
    }


    // CURRENT USER
    private UUID getLoggedInUserId() {

        /*
         * Replace this with Spring Security/JWT.
         */

        throw new UnsupportedOperationException(
                "Implement getLoggedInUserId() using Spring Security"
        );
    }
}
