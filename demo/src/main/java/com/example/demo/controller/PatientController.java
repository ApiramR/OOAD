package com.example.demo.controller;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.example.demo.model.Patient;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PatientService;
import org.springframework.ui.Model;

@Controller
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @RequestMapping(value="/patient",method = RequestMethod.GET)
    String PatientDashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authenticated User roles: " + authentication.getAuthorities());   
        }
        if (authentication != null){
            String username = authentication.getName();
            Patient patient = patientService.getPatientByUsername(username);
            String profilepicpath = "/images/";
            String profilepicture = profilepicpath + patient.getProfilepicture();

            String[] fields = patientService.getFields();
            Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
            System.out.println(patient.getDOB());
            System.out.println(patientDict.get("DOB"));
            patientDict.put("profilepic",profilepicture);
            model.addAttribute("patient",patientDict);
        }
        else{
            return "redirect:/login";
        }
        return "/PatientDashboard";
    }
    @GetMapping("/test-date")
    public ResponseEntity<?> testDateHandling() {
        Map<String, Object> testData = new HashMap<String,Object>();
        testData.put("date", LocalDate.now());  // Use LocalDate
        return ResponseEntity.ok(testData);  // Return as response
    }
    @RequestMapping(value = "/patient/debug", method = RequestMethod.GET)
    public String debugPatientAccess() {
        return "Debugging Patient Access";
    }
}
