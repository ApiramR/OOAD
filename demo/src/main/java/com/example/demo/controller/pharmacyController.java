package com.example.demo.controller;


import com.example.demo.model.Patient;
import com.example.demo.model.Pharmacy;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;



@Controller
public class pharmacyController {
    @Autowired
    private final PharmacyService pharmacyService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    public pharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(value="/pharmacy",method = RequestMethod.GET)
    String PharmacyDashboard(Model model){
        if (Auth(model)) return "redirect:/login";
        return "Pharmacy/pharmacy-dashboard";
    }

    private boolean Auth(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authenticated User roles: " + authentication.getAuthorities());
        }
        if (authentication != null){
            String username = authentication.getName();
            Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
            String profilepicture = "/images/" + pharmacy.getProfilepicture();

            String[] fields = pharmacyService.getFields();
            Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, fields);
            pharmacyDict.put("profilepic",profilepicture);
            model.addAttribute("pharmacy",pharmacyDict);
        }
        else{
            return true;
        }
        return false;
    }

    //Accessing Pharmacy Inventory
    @RequestMapping(value="/pharmacy/{username}/inventory")
    public String pharmacyInventory(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        String profilePicture = "/images/" + pharmacy.getProfilepicture();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, pharmacyService.getFields());
        pharmacyDict.put("profilepic",profilePicture);
        model.addAttribute("pharmacy",pharmacyDict);
        model.addAttribute("age",Period.between(pharmacy.getDOB(), LocalDate.now()).getYears());
        return "Pharmacy/pharmacy-inventory";
    }

    //Accessing Pharmacy Prescriptions
    @RequestMapping(value="/pharmacy/{username}/prescriptions")
    public String pharmacyPrescriptions(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        return "Pharmacy/pharmacy-prescriptions";
    }


    //Accessing Pharmacy Profile
    @RequestMapping(value="/pharmacy/{username}/profile")
    public String pharmacyProfile(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        String profilePicture = "/images/" + pharmacy.getProfilepicture();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, pharmacyService.getFields());
        pharmacyDict.put("profilepic",profilePicture);
        model.addAttribute("pharmacy",pharmacyDict);
        model.addAttribute("age",Period.between(pharmacy.getDOB(), LocalDate.now()).getYears());
        return "Pharmacy/pharmacy-profile";
    }



    //Dashboard for seconds
    @RequestMapping(value="/pharmacy/{username}",method = RequestMethod.GET)
    String urlDashboard(Model model){
        if (Auth(model)) return "redirect:/login";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);

        String[] fields = pharmacyService.getFields();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, fields);

        return "/Pharmacy/pharmacy-dashboard";
    }

    @GetMapping(value="/pharmacy/settings")
    public String patientSettings(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return "redirect:/login?loginagain";
        }
        String username = authentication.getName();
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        initialize(username, model, pharmacy);
        return "PatientSettings.html";
    }


    @PostMapping("/pharmacy/settings")
    @ResponseBody
    public ResponseEntity<?> PharmacyChangeSettings(@RequestParam Map<String, String> formData, @RequestParam(value = "profilepicture", required = false) MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        String username = authentication.getName();
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        if (file != null){
            String currentReportpath = imageuploaddir + pharmacy.getProfilepicture();
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
                pharmacy.setProfilepicture(filename);
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
                    Method setter = Pharmacy.class.getMethod(methodName, Double.class);
                    setter.invoke(pharmacy,Double.parseDouble(res));
                    continue;
                }
                Method setter = Pharmacy.class.getMethod(methodName, String.class);
                setter.invoke(pharmacy,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        pharmacyService.updatePharmacy(pharmacy);
        return ResponseEntity.ok("Update successful");
    }



    @RequestMapping(value = "/pharmacy/debug", method = RequestMethod.GET)
    public String debugPatientAccess() {
        return "Debugging Pharmacy Access";
    }
    public void initialize(String username,Model model,Pharmacy pharmacy){
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + pharmacy.getProfilepicture();
        String[] fields = pharmacyService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(pharmacy, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("patient",patientDict);
    }

}