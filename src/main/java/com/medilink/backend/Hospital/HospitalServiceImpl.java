package com.medilink.backend.Hospital;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Hospital.Enum.HospitalStatus;
import com.medilink.backend.Hospital.dto.HospitalRequest;
import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.Hospital.dto.HospitalUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService{

    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional
    public HospitalResponse getMyProfile() {
        UUID userId = getLoggedInUserId();
        HospitalEntity hospital = hospitalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Hospital profile not found")
                );

        return convertToHospitalResponse(hospital);
    }

    @Override
    @Transactional
    public HospitalResponse updateMyProfile(HospitalUpdateRequest request) {

        UUID userId = getLoggedInUserId();

        HospitalEntity hospital = hospitalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Hospital profile not found")
                );

        if (hospital.getStatus() == HospitalStatus.INACTIVE) {
            throw new RuntimeException("Hospital is inactive");
        }
        if (request.getHospitalName() != null) {
            hospital.setHospitalName(request.getHospitalName());
        }
        if (request.getAddress() != null) {
            hospital.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            hospital.setCity(request.getCity());
        }
        if (request.getState() != null) {
            hospital.setState(request.getState());
        }
        if (request.getCountry() != null) {
            hospital.setCountry(request.getCountry());
        }
        if (request.getWebsite() != null) {
            hospital.setWebsite(request.getWebsite());
        }
        return convertToHospitalResponse(hospital);
    }

    @Override
    @Transactional
    public void deactivateMyProfile() {
        UUID userId = getLoggedInUserId();

        HospitalEntity hospital = hospitalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Hospital profile not found")
                );

        if (hospital.getStatus() == HospitalStatus.INACTIVE) {
            throw new RuntimeException("Hospital is already inactive");
        }

        hospital.setStatus(HospitalStatus.INACTIVE);
    }

    @Override
    @Transactional
    public HospitalResponse getHospitalById(UUID hospitalId) {
        HospitalEntity hospital =hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException( "Hospital not found with ID: " + hospitalId));

        if (hospital.getStatus() == HospitalStatus.INACTIVE) {
            throw new RuntimeException("Hospital is inactive");
        }

        return convertToHospitalResponse(hospital);
    }

    @Override
    @Transactional
    public List<HospitalResponse> getHospitalByParams(String name, String city, String state, String country) {
        return hospitalRepository.searchHospitals(name,city,state,country)
                .stream()
                .map(hospitalEntity -> convertToHospitalResponse(hospitalEntity))
                .toList();
    }

    private UUID getLoggedInUserId() {
//        Authentication authentication =
//                SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();
//
//        if (authentication == null ||
//                !authentication.isAuthenticated()) {
//
//            throw new RuntimeException("User is not authenticated");
//        }

        return UUID.fromString("..kklv");
    }

    private HospitalResponse convertToHospitalResponse(HospitalEntity hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .hosiptalName(hospital.getHospitalName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .state(hospital.getState())
                .country(hospital.getCountry())
                .website(hospital.getWebsite())
                .status(hospital.getStatus())
                .build();
    }
}
