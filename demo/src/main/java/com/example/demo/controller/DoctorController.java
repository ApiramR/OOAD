package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorService;

@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @GetMapping("{id}")
    public ResponseEntity<Doctor> getPatientById(@PathVariable("id") Long DocID){
        Doctor doctor = doctorService.getDoctorByID(DocID);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping(value="/doctor")
    public String getMethodName() {
        return "dashboard.html";
    }
    


}
