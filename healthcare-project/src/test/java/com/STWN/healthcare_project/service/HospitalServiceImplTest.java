package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.entity.Hospital;
import com.STWN.healthcare_project.model.HospitalRequest;
import com.STWN.healthcare_project.model.HospitalResponse;
import com.STWN.healthcare_project.repository.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HospitalServiceImplTest {
    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private HospitalServiceImpl hospitalService;

    private HospitalRequest hospitalRequest;
    private Hospital hospital;
    private HospitalResponse hospitalResponse;

    @BeforeEach
    void setUp(){
        hospitalRequest = HospitalRequest.builder()
                .name("test hospital")
                .address("123 test st")
                .phone("1235456777")
                .email("test@gmail.com")
                .build();

        hospital = Hospital.builder()
                .id(1L)
                .name("test hospital")
                .address("123 test st")
                .phone("test@1235456777")
                .email("test@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        hospitalResponse = HospitalResponse.builder()
                .id(1L)
                .name("test hospital")
                .address("123 test st")
                .phone("1235456777")
                .email("test@gmail.com")
                .build();
    }

    @Test
    void registerHospital_shouldReturnHospitalResponse(){
        Mockito.when(hospitalRepository.save(Mockito.any(Hospital.class)))
                .thenReturn(hospital);

        HospitalResponse result = hospitalService.create(hospitalRequest);

        assertNotNull(result);
        assertEquals(hospitalResponse.getName(), result.getName());
        assertEquals(hospitalResponse.getAddress(), result.getAddress());
        assertEquals(hospitalResponse.getPhone(), result.getPhone());
        assertEquals(hospitalResponse.getEmail(), result.getEmail());

        /*make sure mocknya (hospital repository) dipanggil sebanyak 1x*/
        Mockito.verify(hospitalRepository, Mockito.times(1)).save(Mockito.any(Hospital.class));
    }
}