package com.medilink.backend.Hospital;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Hospital.dto.HospitalRequest;
import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.Hospital.dto.HospitalUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody @Valid HospitalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.createHospital(request));
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponse> getHospital(@PathVariable UUID hospitalId) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getHospitalById(hospitalId));
    }

    @GetMapping()
    public ResponseEntity<List<HospitalResponse>> getHospitalByParam(@RequestParam(required = false) String name,
                                                                     @RequestParam(required = false) String city,
                                                                     @RequestParam(required = false) String state,
                                                                     @RequestParam(required = false) String country) {
        name = normalize(name);
        city = normalize(city);
        state = normalize(state);
        country = normalize(country);
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getHospitalByParams(name,city,state,country));
    }

    @PatchMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponse> updateHospital(
            @PathVariable UUID hospitalId,
            @RequestBody @Valid HospitalUpdateRequest request) {
        return ResponseEntity.ok(hospitalService.updateHospital(hospitalId, request));
    }

    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> deleteHospital(@PathVariable UUID hospitalId) {
        hospitalService.deleteHospitalById(hospitalId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hospitalId}/activate")
    public ResponseEntity<HospitalResponse> activateHospital(
            @PathVariable UUID hospitalId) {
        return ResponseEntity.ok(hospitalService.activateHospital(hospitalId));
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
