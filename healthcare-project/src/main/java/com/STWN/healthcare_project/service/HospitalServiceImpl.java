package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.ResourceNotFoundException;
import com.STWN.healthcare_project.entity.Hospital;
import com.STWN.healthcare_project.model.HospitalRequest;
import com.STWN.healthcare_project.model.HospitalResponse;
import com.STWN.healthcare_project.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;
    private final CacheService cacheService;

    private static String HOSPITA_CACHE_KEY="hospital:key:hospital:";

    @Override
    public Page<HospitalResponse> search(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public HospitalResponse get(Long id) {
        String key = HOSPITA_CACHE_KEY + id;
        return cacheService.get(key, HospitalResponse.class)
                .orElseGet(()->{
                    return hospitalRepository.findById(id).map(this::convertToResponse)
                            .orElseThrow(()-> new ResourceNotFoundException("hospital with id " + id + "is not found"));
                });
    }

    @Override
    public HospitalResponse update(Long id, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hospital with id " + id + "is not found"));
        updateHospitalFromRequest(hospital, request);

        hospitalRepository.save(hospital);
        return convertToResponse(hospital);
    }

    @Override
    public HospitalResponse create(HospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .build();
        updateHospitalFromRequest(hospital, request);
        hospitalRepository.save(hospital);
        return convertToResponse(hospital);
    }

    @Override
    public void deleteHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hospital with id " + id + "is not found"));
        hospitalRepository.delete(hospital);
    }

    private HospitalResponse convertToResponse(Hospital hospital){
        return HospitalResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .phone(hospital.getPhone())
                .email(hospital.getEmail())
                .description(hospital.getDescription())
                .build();
    }
    
    private void updateHospitalFromRequest(Hospital hospital, HospitalRequest request){
        hospital.setName(request.getName());
        hospital.setAddress(request.getAddress());
        hospital.setPhone(request.getPhone());
        hospital.setEmail(request.getEmail());
        hospital.setDescription(request.getDescription());
    }
}
