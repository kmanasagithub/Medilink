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
    public HospitalResponse createHospital(HospitalRequest request) {

        HospitalEntity hospital = HospitalEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .status(HospitalStatus.ACTIVE)
                .build();

        HospitalEntity savedHospital = hospitalRepository.save(hospital);

        return convertToHospitalResponse(savedHospital);
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

    @Override
    @Transactional
    public void deleteHospitalById(UUID hospitalId) {
        HospitalEntity hospital =hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException( "Hospital not found with ID: " + hospitalId));

        if (hospital.getStatus() == HospitalStatus.INACTIVE) {
            throw new RuntimeException("Hospital is already Active");
        }

        hospital.setStatus(HospitalStatus.INACTIVE);
    }

    @Override
    @Transactional
    public HospitalResponse updateHospital(UUID hospitalId, HospitalUpdateRequest request) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException( "Hospital not found with ID: " + hospitalId));

        if (hospital.getStatus() == HospitalStatus.INACTIVE) {
            throw new RuntimeException("Hospital is inactive");
        }

        if (request.getName() != null) {
            hospital.setName(request.getName());
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

        if (request.getPhone() != null) {
            hospital.setPhone(request.getPhone());
        }

        if (request.getWebsite() != null) {
            hospital.setWebsite(request.getWebsite());
        }

        return convertToHospitalResponse(hospital);
    }

    @Override
    @Transactional
    public HospitalResponse activateHospital(UUID hospitalId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException( "Hospital not found with ID: " + hospitalId));

        if(hospital.getStatus() == HospitalStatus.ACTIVE) {
            throw new RuntimeException("Hospital is already inactive");
        }

        hospital.setStatus(HospitalStatus.ACTIVE);
        return convertToHospitalResponse(hospital);
    }


    private HospitalResponse convertToHospitalResponse(HospitalEntity hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .state(hospital.getState())
                .country(hospital.getCountry())
                .phone(hospital.getPhone())
                .website(hospital.getWebsite())
                .status(hospital.getStatus())
                .build();
    }
}
