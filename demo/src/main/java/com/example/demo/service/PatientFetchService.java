package com.example.demo.service;


import com.example.demo.model.Patient;

public interface  PatientFetchService {
    Patient getPatientById(Long PatientID);
}
