package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.entity.Hospital;
import com.STWN.healthcare_project.model.HospitalRequest;
import com.STWN.healthcare_project.model.HospitalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HospitalService {
    Page<HospitalResponse> search(String keyword, Pageable pageable);
    HospitalResponse get(Long id);
    HospitalResponse update(Long id, HospitalRequest request);
    HospitalResponse create(HospitalRequest request);
    void deleteHospital(Long id);
}
