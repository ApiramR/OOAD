package com.example.demo.controller;

import com.example.demo.model.Userr;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Map;

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
        model.addAttribute("user", new Userr());
        return "register";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value="/register")
    public String adduser(@RequestParam Map<String, String> formData){
        String valid = "success";
        if (formData.get("typ").equals("Doctor")){
            Doctor doctor = new Doctor();
            try{
                for (String field : doctorService.getFields()){
                    if (formData.get(field) == null){
                        continue;
                    }
                    String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);

                    // Get the setter method for the field
                    
                    // Invoke the setter method, passing the value from formData
                    if (field.equals("DOB")){
                        LocalDate dob = LocalDate.parse(formData.get(field));
                        Method setter = Doctor.class.getMethod(methodName, LocalDate.class);
                        setter.invoke(doctor, dob);
                        continue;
                    }
                    String res = formData.get(field);
                    if (field.equals("Password")){
                        res = passwordEncoder.encode(res);
                    }
                    Method setter = Doctor.class.getMethod(methodName, String.class);
                    setter.invoke(doctor,res);
                }
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
            valid = doctorService.addDoctor(doctor);
        }
        else if (formData.get("typ").equals("Supplier")){
            Supplier supplier = new Supplier();
            try{
                for (String field : supplierService.getFields()){
                    if (formData.get(field) == null){
                        continue;
                    }
                    String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);

                    // Get the setter method for the field
            
                    // Invoke the setter method, passing the value from formData
                    if (field.equals("DOB")){
                        LocalDate dob = LocalDate.parse(formData.get(field));
                        Method setter = Supplier.class.getMethod(methodName, LocalDate.class);
                        setter.invoke(supplier, dob);
                        continue;
                    }
                    String res = formData.get(field);
                    if (field.equals("Password")){
                        res = passwordEncoder.encode(res);
                    }
                    Method setter = Supplier.class.getMethod(methodName, String.class);
                    setter.invoke(supplier,res);
                }
            }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
            valid = supplierService.addSupplier(supplier);
        }
        else if (formData.get("typ").equals("Patient")){
            for (String field:patientService.getFields()){
                System.out.println(field + " " + formData.get(field));
            }
            Patient patient = new Patient();
            try{
                for (String field : patientService.getFields()){
                    if (formData.get(field) == null){
                        continue;
                    }
                    String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);

                    // Get the setter method for the field
                    if (field.equals("DOB")){
                        LocalDate dob = LocalDate.parse(formData.get(field));
                        Method setter = Patient.class.getMethod(methodName, LocalDate.class);
                        setter.invoke(patient, dob);
                        continue;
                    }
                    String res = formData.get(field);
                    if (field.equals("Password")){
                        System.out.println(res);
                        res = passwordEncoder.encode(res);
                        System.out.println(res);
                    }
                    Method setter = Patient.class.getMethod(methodName, String.class);
                    setter.invoke(patient,res);
                }
            }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
            valid = patientService.addPatient(patient);
        }
        else{
            System.out.println("Hello");
            Pharmacy pharmacy = new Pharmacy();
            try{
                for (String field : pharmacyService.getFields()){
                    if (formData.get(field) == null){
                        continue;
                    }
                    String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);

                    // Get the setter method for the field
                    
                    // Invoke the setter method, passing the value from formData
                    if (field.equals("DOB")){
                        LocalDate dob = LocalDate.parse(formData.get(field));
                        Method setter = Pharmacy.class.getMethod(methodName, LocalDate.class);
                        setter.invoke(pharmacy, dob);
                        continue;
                    }
                    String res = formData.get(field);
                    if (field.equals("Password")){
                        res = passwordEncoder.encode(res);
                    }
                    Method setter = Pharmacy.class.getMethod(methodName, String.class);
                    setter.invoke(pharmacy,res);
                }
            }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
            valid = pharmacyService.addPharmacy(pharmacy);
        }
        if (valid.equals("success"))
            return "redirect:/login";
        else{   
            return "redirect:/register?" + valid;
        }
    }
}
