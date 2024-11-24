package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorService;

@Controller
@RequestMapping("api/doctorDetails")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @GetMapping("{id}")
    public ResponseEntity<Doctor> getPatientById(@PathVariable("id") Long DocID){
        Doctor doctor = doctorService.getDoctorByID(DocID);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping(value="/doctor")
    public String getMethodName(@RequestParam String param) {
        return "dashboard.html";
    }
    


}
