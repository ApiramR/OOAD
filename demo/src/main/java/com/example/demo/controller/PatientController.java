package com.example.demo.controller;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.model.Report;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PatientService;
import com.example.demo.service.ReportService;

import org.springframework.ui.Model;

@Controller
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    //patient dashboard
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
            initialize(username,model,patient);
            return "redirect:/patient/" + username + "/profile";
        }
        else{
            return "redirect:/login";
        }
    }
    //report dashboard
    @GetMapping(value="/patient/{username}/reports")
    public String reportDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        List<Report>reports = patient.getReports();
        String reportpath = "/reports/";
        initialize(username, model, patient);
        model.addAttribute("reports",reports);
        model.addAttribute("reportpath",reportpath);
        return "PatientReports.html";
    }
    //Requesting prescriptions
    @RequestMapping(value="/patient/{username}/prescriptions")
    public String prescriptionDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        List<Prescription>prescriptions = patient.getPrescriptions();
        String prescriptionpath = "/prescription/";
        initialize(username, model, patient);
        model.addAttribute("reports",prescriptions);//change it to prescriptions
        model.addAttribute("prescriptionpath",prescriptionpath);
        return "PatientPrescriptions.html";
    }
    //Patientprofile
    @GetMapping(value="/patient/{username}/profile")
    public String patientProfile(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Patient patient = patientService.getPatientByUsername(username);
        initialize(username, model, patient);
        model.addAttribute("age",Period.between(patient.getDOB(), LocalDate.now()).getYears());
        return "PatientProfile.html";
    }
    //Show patient settings
    @GetMapping(value="/patient/settings")
    public String patientSettings(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return "redirect:/login?loginagain";
        }
        String username = authentication.getName();
        Patient patient = patientService.getPatientByUsername(username);
        initialize(username, model, patient);
        return "PatientSettings.html";
    }

    //add reports
    @PostMapping(value="/patient/{username}/reports")
    public String patientAddReports(Model model,@PathVariable String username, @RequestParam String title, @RequestParam MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        String added = reportService.addReport(file,title,patient);
        model.addAttribute("success",added);  
        List<Report>reports = patient.getReports();
        String reportpath = "/reports/";
        initialize(username, model, patient);
        model.addAttribute("reports",reports);
        model.addAttribute("reportpath",reportpath);
        return "PatientReports.html";
    }
    @RequestMapping(value="/patient/{username}/reports/remove/{reportid}")
    public String patientremoveReports(Model model,@PathVariable String username, @PathVariable Long reportid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        Report report = reportService.getReportByID(reportid);
        String reportpath = "/reports/";
        String currentReportpath = uploadDir + report.getReportfile();
        File file = new File(currentReportpath);
        if (file.exists()){
            file.delete();
        }
        reportService.deleteReport(reportid);
        List<Report>reports = patient.getReports();
        initialize(username, model, patient);
        model.addAttribute("reports",reports);
        model.addAttribute("reportpath",reportpath);
        return "PatientReports.html";
    }
    @GetMapping("/reports/{filename}")
    public ResponseEntity<?> getReportFile(@PathVariable String filename) {
        File file = new File(uploadDir + File.separator + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(file));
    }
    @PostMapping("/patient/settings")
    @ResponseBody
    public ResponseEntity<?> PatientChangeSettings(@RequestParam Map<String, String> formData,@RequestParam(value = "profilepicture", required = false) MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        Map<String,String> response = new HashMap<String,String>();
        String username = authentication.getName();
        Patient patient = patientService.getPatientByUsername(username);
        if (file != null && !file.isEmpty()){
            System.out.println("Yes iam changing profile picture");
            patientService.Saveprofilepicture(file,patient);
        }
        if (patientService.updatePatient(formData,patient)){
            response.put("Fname",patient.getFname());
            response.put("Mname",patient.getMname());
            response.put("Lname",patient.getLname());
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");;    
        }
    }
    @RequestMapping(value = "/patient/debug", method = RequestMethod.GET)
    public String debugPatientAccess() {
        return "Debugging Patient Access";
    }
    public void initialize(String username,Model model,Patient patient){
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + patient.getProfilepicture();
        String[] fields = patientService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);    
    }
}
