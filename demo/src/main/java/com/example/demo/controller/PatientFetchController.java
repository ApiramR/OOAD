package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Patient;
import com.example.demo.service.PatientFetchService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/patientDetails")
public class PatientFetchController {

    private final PatientFetchService patientFetchService;
    
    @GetMapping("{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long PatientID){
        Patient patient = patientFetchService.getPatientById(PatientID);
        return ResponseEntity.ok(patient);
    }


}
