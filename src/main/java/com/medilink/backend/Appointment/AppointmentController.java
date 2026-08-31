package com.medilink.backend.Appointment;

import com.medilink.backend.Appointment.dto.AppointmentRequest;
import com.medilink.backend.Appointment.dto.AppointmentResponse;
import com.medilink.backend.Appointment.dto.AvailableSlotResponse;
import com.medilink.backend.Availability.Enum.ConsultationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class AppointmentController {

    private final AppointmentService appointmentService;

    // AVAILABLE SLOTS
    @GetMapping("/doctors/{doctorId}/available-slots")
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlots(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date,
            @RequestParam ConsultationType consultationType
    ) {

        return ResponseEntity.ok(
                appointmentService.getAvailableSlots(doctorId, date, consultationType)
        );
    }

    // CREATE APPOINTMENT
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createAppointment(request));
    }

    // GET APPOINTMENT
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
    }

    // PATIENT APPOINTMENTS
    @GetMapping("/patients/me/appointments")
    public ResponseEntity<Page<AppointmentResponse>> getMyAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getMyAppointments(pageable));
    }

    // DOCTOR APPOINTMENTS
    @GetMapping("/doctors/me/appointments")
    public ResponseEntity<Page<AppointmentResponse>> getDoctorAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(pageable));
    }

    // CANCEL
    @PatchMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable UUID appointmentId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, reason));
    }

    // DOCTOR JOIN
    @PostMapping("/appointments/{appointmentId}/join")
    public ResponseEntity<AppointmentResponse> joinAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.joinAppointment(appointmentId));
    }

    // COMPLETE
    @PatchMapping("/appointments/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.completeAppointment(appointmentId));
    }
}