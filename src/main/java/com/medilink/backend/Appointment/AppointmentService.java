package com.medilink.backend.Appointment;

import com.medilink.backend.Appointment.dto.AppointmentRequest;
import com.medilink.backend.Appointment.dto.AppointmentResponse;
import com.medilink.backend.Appointment.dto.AvailableSlotResponse;
import com.medilink.backend.Availability.Enum.ConsultationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    List<AvailableSlotResponse> getAvailableSlots(
            UUID doctorId,
            LocalDate date,
            ConsultationType consultationType
    );

    AppointmentResponse createAppointment(
            AppointmentRequest request
    );

    AppointmentResponse getAppointment(
            UUID appointmentId
    );

    Page<AppointmentResponse> getMyAppointments(
            Pageable pageable
    );

    Page<AppointmentResponse> getDoctorAppointments(
            Pageable pageable
    );

    AppointmentResponse cancelAppointment(
            UUID appointmentId,
            String reason
    );

    AppointmentResponse joinAppointment(
            UUID appointmentId
    );

    AppointmentResponse completeAppointment(
            UUID appointmentId
    );

}
