package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.model.Patient;
import com.example.demo.repo.PatientFetchDao;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class PatientFetchServiceImpl implements PatientFetchService{
    private final PatientFetchDao patientFetchDao;

    @SuppressWarnings("serial")
    public static class ResourceNotFound extends RuntimeException {
        public ResourceNotFound(String message) {
            super(message);
        }
    }

    @Override
    public Patient getPatientById(Long PatientID) {
        Patient patient=patientFetchDao.findById(PatientID)
            .orElseThrow(()->
              new ResourceNotFound("patient does exist"));
        return patient;//AppointmentsMapper.mapToPatientFetchDto(patientModel);
    }

    

}
