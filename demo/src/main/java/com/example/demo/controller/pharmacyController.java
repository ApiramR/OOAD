package com.example.demo.controller;


import com.example.demo.model.Pharmacy;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;


@Controller
public class pharmacyController {
    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    public pharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }


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

    @RequestMapping(value="/pharmacy/{username}/ordermedicine")
    public String pharmacyOrderMedicine(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        String profilePicture = "/images/" + pharmacy.getProfilepicture();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, pharmacyService.getFields());
        pharmacyDict.put("profilepic",profilePicture);
        model.addAttribute("pharmacy",pharmacyDict);
        model.addAttribute("age",Period.between(pharmacy.getDOB(), LocalDate.now()).getYears());
        return "Pharmacy/pharmacy-ordermedicine";
    }

    @RequestMapping(value="/pharmacy/{username}/prescriptions",method = RequestMethod.GET)
    String pharmacyPrescriptions(Model model){
        if (Auth(model)) return "redirect:/login";
        return "/Pharmacy/pharmacy-prescriptions";
    }

    //Dashboard for seconds
    @RequestMapping(value="/pharmacy/{username}",method = RequestMethod.GET)
    String urlDashboard(Model model){
        if (Auth(model)) return "redirect:/login";
        return "/Pharmacy/pharmacy-dashboard";
    }

    @RequestMapping(value="/pharmacy/settings",method = RequestMethod.GET)
    public String pharmacySettings(Model model){
        if (Auth(model)) return "redirect:/login";
        return "Pharmacy/pharmacy-settings";
    }
    
    @PostMapping("/pharmacy/settings")
    @ResponseBody
    public ResponseEntity<?> PatientChangeSettings(@RequestParam Map<String, String> formData){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }

        System.out.println("Yo i am here");
        Map<String,Object> response = new HashMap<String,Object>();
        String username = authentication.getName();
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        System.out.println(username);
        if (pharmacyService.updatePharmacy(formData,pharmacy)){
            String[] fields = pharmacyService.getFields();
            response = modelMapperUtil.mapFieldsToGetters(pharmacy, fields);
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

    // @PutMapping("pharmacy/{username}/settings")
    // public ResponseEntity<?> updatePharmacySettings(
    //         @PathVariable String username,
    //         @RequestBody Map<String, String> updatedData) {
    //     Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
    //     if (pharmacy == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pharmacy not found");
    //     }
    //     // Update fields from the request
    //     pharmacy.setFname(updatedData.get("Fname"));
    //     pharmacy.setLname(updatedData.get("Lname"));
    //     pharmacy.setPno(updatedData.get("pno"));
    //     pharmacy.setAddress(updatedData.get("address"));
    //     pharmacy.setOpeningHours(updatedData.get("openingHours"));

    //     // Save the updated pharmacy
    //     pharmacyService.addPharmacy(pharmacy);
    //     return ResponseEntity.ok("Settings updated successfully");
    // }
    @RequestMapping(value = "/pharmacy/debug", method = RequestMethod.GET)
    public String debugPharmacyAccess() {
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