package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Patient;
import com.example.demo.repo.PatientRepo;

@Service
public class PatientService {
    @Autowired
    PatientRepo rep;

    public void addPatient(Patient patient){
        rep.save(patient);
    }
    
    public void updatePatient(Patient patient){
        rep.save(patient);
    }

    public Patient getPatient(int patientID){
        return rep.findById(patientID).orElse(new Patient());
    } 

    public void deletePatient(int patientID){
        rep.deleteById(patientID);
    }
}
