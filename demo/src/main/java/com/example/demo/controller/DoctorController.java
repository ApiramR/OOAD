package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientCommentsService;
import com.example.demo.service.PatientService;

@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientCommentsService patientCommentsService;
    
    @GetMapping("{id}")
    public ResponseEntity<Doctor> getPatientById(@PathVariable("id") Long DocID){
        Doctor doctor = doctorService.getDoctorByID(DocID);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping(value="/doctor")
    public String getMethodName() {
        return "dashboard.html";
    }
    @PostMapping(value="/doctor/{pid}/comment")
    public String commentPatient(@PathVariable Long pid,@RequestParam String comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Doctor doctor = doctorService.getDoctorByUsername(authentication.getName());
        patientCommentsService.addComments(comment,patientService.getPatientByID(pid),doctor);
        return "redirect:/doctor";
    }
}
