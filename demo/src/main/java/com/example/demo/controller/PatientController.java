package com.example.demo.controller;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
    @RequestMapping(value="/patient/{username}/profile")
    public String patientProfile(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Patient patient = patientService.getPatientByUsername(username);
        initialize(username, model, patient);
        model.addAttribute("age",Period.between(patient.getDOB(), LocalDate.now()).getYears());
        return "PatientProfile.html";
    }
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

    @PostMapping(value="/patient/{username}/reports")
    public String patientAddReports(Model model,@PathVariable String username, @RequestParam String title, @RequestParam MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Patient patient = patientService.getPatientByUsername(username);
        
        try{
            long l = System.currentTimeMillis();
            String s = l + "";
            String filename = 'a' + s + '_' + file.getOriginalFilename();
            File directory = new File(uploadDir);
            if (!directory.exists()){
                directory.mkdirs();
            }
            File destinationFile = new File(directory,filename);
            Report report = new Report();
            report.setTitle(title);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            report.setCreatedDate(formattedDate);
            report.setUpdatedDate(formattedDate);
            report.setReportfile(filename);
            report.setPatient(patient);
            String added = reportService.addReport(report);
            model.addAttribute("success",added);
            if (added.equals("Uploaded Successfully")){
                file.transferTo(destinationFile);
            }
        }catch(IOException e){
            e.printStackTrace();
            model.addAttribute("success","Error Occured Please Try Again!");
        }
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
        String username = authentication.getName();
        Patient patient = patientService.getPatientByUsername(username);
        if (file != null){
            String currentReportpath = imageuploaddir + patient.getProfilepicture();
            File currentpicture = new File(currentReportpath);
            if (currentpicture.exists()){
                System.out.println("Why its not deletingggg");
                currentpicture.delete();
            }
            long l = System.currentTimeMillis();
            String s = l + "";
            String filename = 'a' + s + '_' + file.getOriginalFilename();
            System.out.println(file.getOriginalFilename());
            File directory = new File(imageuploaddir);
            if (!directory.exists()){
                directory.mkdirs();
            }
            try{
                File destinationFile = new File(directory,filename);
                patient.setProfilepicture(filename);    
                file.transferTo(destinationFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try{
            for (Map.Entry<String, String> column : formData.entrySet()) {
                String fieldName = column.getKey();
                Object fieldValue = column.getValue();
                if (fieldName.equals("profilepicture")){
                    continue;
                }
                if (fieldValue == null)continue;
                if (fieldValue.equals(""))continue;
                System.out.println(fieldName + " " + (String)fieldValue);
                    String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                    String res = (String)fieldValue;
                    if (fieldName.equals("Password")){
                        System.out.println(res);
                        res = passwordEncoder.encode(res);
                        System.out.println(res);
                    }
                    else if (fieldName.equals("height") || fieldName.equals("weight")){
                        Method setter = Patient.class.getMethod(methodName, Double.class);
                        setter.invoke(patient,Double.parseDouble(res));
                        continue;    
                    }
                    Method setter = Patient.class.getMethod(methodName, String.class);
                    setter.invoke(patient,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        patientService.updatePatient(patient);
        return ResponseEntity.ok("Update successful");
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
