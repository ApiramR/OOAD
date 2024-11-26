package com.example.demo.controller;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Doctor;
import com.example.demo.model.Medicine;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.model.Report;
import com.example.demo.service.DoctorService;
import com.example.demo.service.MedicineService;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PatientCommentsService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PrescriptionService;


@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private MedicineService medicineService;
    @Autowired
    private PatientService patientService;
    @Autowired 
    private PatientCommentsService patientCommentsService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;


    //add reports
    @PostMapping(value="/doctor/{username}/patient")
    public String doctorAddReports(Model model,@PathVariable String username, @RequestParam Long patientID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Doctor doctor = doctorService.getDoctorByUsername(username);
        Patient patient = patientService.getPatientByID(patientID);
        if (patient == null){
            model.addAttribute("success","Patient not exist");
        }
        else{
            List<Report>reports = patient.getReports();
            String reportpath = "/reports/";
            model.addAttribute("reports",reports);
            model.addAttribute("reportpath",reportpath);
            String[] fields = patientService.getFields();
            Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(patient, fields);
            model.addAttribute("patient",patientDict);  
            model.addAttribute("age",Period.between(patient.getDOB(), LocalDate.now()).getYears());
            model.addAttribute("patientID",patientID.toString());
            System.out.println(patientID);
            System.out.println(patient.getPatientID());
            model.addAttribute("success","Found Successfully");  
        }
        initialize(username, model, doctor);
        return "Doctor/DoctorPatient.html";
    }
    @GetMapping(value="/doctor/{username}/patient")
    public String reportDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Doctor doctor = doctorService.getDoctorByUsername(username);
        initialize(username, model, doctor);
        return "Doctor/DoctorPatient.html";
    }
    @RequestMapping(value="/doctor",method = RequestMethod.GET)
    String doctorDashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authenticated User roles: " + authentication.getAuthorities());   
        }
        if (authentication != null){
            String username = authentication.getName();
            Doctor doctor = doctorService.getDoctorByUsername(username);
            initialize(username,model,doctor);
            return "redirect:/doctor/" + username + "/profile";
        }
        else{
            return "redirect:/login";
        }
    }
    @RequestMapping(value="/doctor/{username}/profile")
    public String doctorProfile(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Doctor doctor = doctorService.getDoctorByUsername(username);
        initialize(username, model, doctor);
        model.addAttribute("age",Period.between(doctor.getDOB(), LocalDate.now()).getYears());
        return "Doctor/DoctorProfile.html";
    }

    @GetMapping(value="/doctor/settings")
    public String doctorSettings(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return "redirect:/login?loginagain";
        }
        String username = authentication.getName();
        Doctor doctor = doctorService.getDoctorByUsername(username);
        initialize(username, model, doctor);
        return "Doctor/DoctorSettings.html";
    }
    @PostMapping("/doctor/settings")
    @ResponseBody
    public ResponseEntity<?> doctorChangeSettings(@RequestParam Map<String, String> formData,@RequestParam(value = "profilepicture", required = false) MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        Map<String,String> response = new HashMap<String,String>();
        String username = authentication.getName();
        Doctor doctor = doctorService.getDoctorByUsername(username);
        if (file != null && !file.isEmpty()){
            System.out.println("Yes iam changing profile picture");
            doctorService.Saveprofilepicture(file,doctor);
        }
        if (doctorService.updatedoctor(formData,doctor)){
            response.put("Fname",doctor.getFname());
            response.put("Mname",doctor.getMname());
            response.put("Lname",doctor.getLname());
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }
    @GetMapping("/doctor/patient/{pID}")
    public String Addprescription(Model model,@PathVariable String pID){
        model.addAttribute("patientID",pID);
        System.out.println(pID);
        return "Doctor/AddPrescription.html";
    }
    @PostMapping("/doctor/patient/{pID}")
    public String AddPrescription(Model model,@PathVariable String pID, @RequestParam Map<String,String>formData){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return "redirect:/login?loginagain";
        }
        String username = authentication.getName();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
        System.out.println(pID);
        System.out.println(formData.get("meds"));
        Medicine medicine = medicineService.getMedBymedName(formData.get("meds"));
        System.out.println("Whats the error here?");
        if (medicine == null){
            System.out.println("Is it really NULL");
            model.addAttribute("prescription","medicine not found, Try again!");
            return "redirect:/doctor/" + username + "/patient";
        }
        Patient patient = patientService.getPatientByID(Long.parseLong(pID));
        Doctor doctor = doctorService.getDoctorByUsername(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            
        Prescription prescription = new Prescription(Integer.parseInt(formData.get("count")),medicine,formData.get("dosage"),formattedDate
                                        ,formData.get("description"),patient,doctor);
        prescriptionService.addPrescription(prescription);
        model.addAttribute("prescription","added sucessfully");
        return "redirect:/doctor/" + username + "/patient";
    }
    @PostMapping(value="/doctor/{pid}/comment")
    public String addComment(Model model, @PathVariable String pid, @RequestParam String comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Doctor doctor = doctorService.getDoctorByUsername(authentication.getName());
        patientCommentsService.addComments(comment,patientService.getPatientByID(Long.parseLong(pid)),doctor);
        model.addAttribute("comment","Added Successfully");
        return "redirect:/doctor/" + username + "/patient";
    }
    public void initialize(String username,Model model,Doctor doctor){
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + doctor.getProfilepicture();
        String[] fields = doctorService.getFields();
        Map<String, Object> doctorDict = modelMapperUtil.mapFieldsToGetters(doctor, fields);
        doctorDict.put("profilepic",profilepicture);
        model.addAttribute("doctor",doctorDict);    
    }
    



    
    


}
