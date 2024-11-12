package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PharmacyService;
import com.example.demo.service.SupplierService;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @GetMapping(value = "/register")
    public String getRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value="/register")
    public String adduser(@ModelAttribute User user){
        if (user.gettyp() == "Doctor"){
            DoctorService.addDoctor(user);
        }
        else if (user.gettyp() == "Supplier"){
            SupplierService.addSupplier(user);
        }
        else if (user.gettyp() == "Patient"){
            PatientService.addPatient(user);
        }
        else{
            PharmacyService.addPharmacy(user);
        }
        return "redirect:/login";
    }
}
