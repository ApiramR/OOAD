package com.example.demo.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class SupplierController {
    @RequestMapping(value="/supplier",method = RequestMethod.GET)
    public String Supplierhomepage(Model model){
        model.addAttribute("name" , "Apiram");
        return "index.html";
    }
    @GetMapping(value="/inventory")
    public String Inventoryhomepage(Model model){
        model.addAttribute("Fname","Yushri");
        return "inventory.html";
    }
    @PostMapping(value ="/inventory/addinventory")
    public String AddInventory(Model model,@RequestParam Map<String,Object> formData){   
        //formdata ->
        return "redirect:/supplier";
    }
    //RequestMapping(value="/{abs}")
    //public String call(Model model,@PathVariable abs)
}
