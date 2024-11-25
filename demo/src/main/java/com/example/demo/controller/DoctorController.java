package com.example.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.repo.PatientRepo;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;





@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @GetMapping(value="/doctor")
    public String getMethodName() {
        return "doctordashboard.html";
    }


    @Autowired
    private PatientRepo patientRepository;

    @GetMapping("/doctor/patient/searchPatient")
    public String  searchPatientById( Model model) {
        Map<String, Object> patientDetails = new HashMap<>();
            patientDetails.put("patientID","");
            patientDetails.put("patientName", "");
            patientDetails.put("age", "");
            patientDetails.put("gender", "");
            patientDetails.put("contact", "");
            model.addAttribute("data",patientDetails);
        return "patient.html";
            
    }
@ResponseBody
@PostMapping("/doctor/patient/searchPatient")
    public ResponseEntity<?> searchPatientById(@RequestParam("patientID") Long patientID) {
    
        // Fetch patient based on the patientID
        System.out.println("uihfghb");
        Patient patient = patientService.getPatientByID(patientID);
        
        Map<String, Object> patientDetails = new HashMap<>();
            

        if (patient != null) {
            System.out.println("uihfghb");
            // Create the response with patient details
            System.out.println(patient.getPatientID());
            patientDetails.put("patientID", patient.getPatientID());
            patientDetails.put("patientName", patient.getUsername());
            patientDetails.put("age", Period.between(patient.getDOB(), LocalDate.now()).getYears());
            patientDetails.put("gender", patient.getGender());
            patientDetails.put("contact", patient.getPno());

            
            return ResponseEntity.ok(patientDetails);
            
            
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        
        
        
            
}



    

    @Autowired
    private PatientService patientService;

    

    


    

    
    
    

    @GetMapping(value="doctor/{id}/username")
    public ResponseEntity<String> getDoctorUsernameById(@PathVariable("id") Long docID) {
    Doctor doctor = doctorService.getDoctorByID(docID); // Fetch doctor details
    String username = doctor.getUsername(); // Assume 'getUsername()' returns the username
    return ResponseEntity.ok(username); // Return username in the response body
    }

    



    
    


}
