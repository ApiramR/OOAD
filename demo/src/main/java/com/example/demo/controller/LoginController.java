package com.example.demo.controller;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PharmacyService;
import com.example.demo.service.SupplierService;

@Controller
public class LoginController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private SupplierService supplierService;

    @GetMapping(value = "/login")
    public String getLoginPage(){
        return "login";
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
    @PostMapping(value = "/login")
    public String Login(@RequestParam String username, @RequestParam String password, @RequestParam String role){

        System.out.println(username + " " + password + " " + role);
        boolean isAuthenticated = false;
        if (role.equals("Doctor")){
            isAuthenticated = doctorService.validate(username, password);
        }
        else if (role.equals("Patient"))    {
            isAuthenticated = patientService.validate(username, password);
        }
        else if (role.equals("Pharmacy")){
            isAuthenticated = pharmacyService.validate(username , password);
        }
        else isAuthenticated = supplierService.validate(username , password);
        
        if (isAuthenticated){
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, getAuthorities(role));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        }
        //redirectAttributes.addFlashAttribute("error", "Invalid Credentials");
        return "redirect:/login?error = InvalidCredientials";
    }

}
