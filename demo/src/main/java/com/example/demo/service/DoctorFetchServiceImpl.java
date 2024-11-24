package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Doctor;
import com.example.demo.repo.DoctorFetchDao;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class DoctorFetchServiceImpl implements DoctorFetchService{
    
    private final DoctorFetchDao doctorFetchDao;
        

    @SuppressWarnings("serial")
    public static class ResourceNotFound extends RuntimeException {
        public ResourceNotFound(String message) {
            super(message);
        }
    }

    @Override
    public Doctor getDoctorById(Long DocID) {
        Doctor doctor=doctorFetchDao.findById(DocID)
            .orElseThrow(()->
              new ResourceNotFound("doctor does exist"));
        return doctor;
    }
    
    
}
