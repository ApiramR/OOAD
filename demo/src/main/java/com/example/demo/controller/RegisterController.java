package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Doctor;
import com.example.demo.model.Pharmacy;
import com.example.demo.model.Supplier;
import com.example.demo.model.Patient;

import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PharmacyService;
import com.example.demo.service.SupplierService;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private SupplierService supplierService;
    
    @GetMapping(value = "/register")
    public String getRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value="/register")
    public String adduser(@ModelAttribute User user){
        if (user.getTyp().equals("Doctor")){
            Doctor doctor = (Doctor) user;
            doctorService.addDoctor(doctor);
        }
        else if (user.getTyp().equals("Supplier")){
            Supplier supplier = (Supplier) user;
            supplierService.addSupplier(supplier);
        }
        else if (user.getTyp().equals("Patient")){
            Patient patient = (Patient)user;
            patientService.addPatient(patient);
        }
        else{
            Pharmacy pharmacy = (Pharmacy)user;
            pharmacyService.addPharmacy(pharmacy);
        }
        return "redirect:/login";
    }
}
