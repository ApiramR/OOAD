package com.example.demo.controller;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.example.demo.model.Patient;
import com.example.demo.model.Report;
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
            patientDict.put("profilepic",profilepicture);
            model.addAttribute("patient",patientDict);
        }
        else{
            return "redirect:/login";
        }
        return "/PatientDashboard";
    }
    @RequestMapping(value="/patient/{username}/reports")
    public String reportDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        List<Report>reports = patient.getReports();
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String reportpath = "/reports/";
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
        model.addAttribute("reports",reports);
        model.addAttribute("reportpath",reportpath);
        return "PatientReports.html";
    }
    @RequestMapping(value="/patient/{username}/prescriptions")
    public String prescriptionDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        List<Report>reports = patient.getReports();
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String prescriptionpath = "/reports/";
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
        model.addAttribute("reports",reports);//change it to prescriptions
        model.addAttribute("prescriptionpath",prescriptionpath);
        return "PatientPrescriptions.html";
    }
    @RequestMapping(value="/patient/{username}/profile")
    public String patientProfile(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            if (authentication == null){
                System.out.println("Authentication is null");
            }
            else{
                System.out.println(username);
                System.out.println(authentication.getName());
            }
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
        model.addAttribute("age",Period.between(patient.getDOB(), LocalDate.now()).getYears());
        return "PatientProfile.html";
    }
    @RequestMapping(value="/patient/{username}/settings")
    public String patientSettings(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
        return "PatientSettings.html";
    }

    /*@RequestMapping(value="/patient/{username}/upload")
    public String patientAddReports(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
        return "PatientAddReports.html";
    }*/

    @RequestMapping(value = "/patient/debug", method = RequestMethod.GET)
    public String debugPatientAccess() {
        return "Debugging Patient Access";
    }
}
