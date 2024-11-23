package com.example.demo.controller;


import com.example.demo.model.Pharmacy;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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



    @RequestMapping(value="/pharmacy",method = RequestMethod.GET)
    String PharmacyDashboard(Model model){
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
            return "redirect:/login";
        }
        return "Pharmacy/pharmacy-dashboard";
    }

    //Accessing Pharmacy Inventory
    @RequestMapping(value="/pharmacy/{username}/inventory")
    public String pharmacyInventory(Model model,@PathVariable String username){
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
        return "Pharmacy/pharmacy-prescriptions";
    }


    //Accessing Pharmacy Profile
    @RequestMapping(value="/pharmacy/{username}/profile")
    public String pharmacyProfile(Model model,@PathVariable String username){
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
            return "redirect:/login";
        }
        String username = authentication.getName();
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);

        String[] fields = pharmacyService.getFields();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, fields);

        return "/Pharmacy/pharmacy-dashboard";
    }

}