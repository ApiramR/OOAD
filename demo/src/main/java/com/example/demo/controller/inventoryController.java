package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Pharmacy;

import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class inventoryController {

    // Simulated in-memory list to store inventory items
    private final List<Inventory> inventoryList = new ArrayList<>();

    // Counter for auto-incrementing InventoryID
    private long inventoryIDCounter = 1;
    @Autowired
    private final PharmacyService pharmacyService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    public inventoryController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @RequestMapping(value="/pharmacy/{username}/inventory/add")
    public String pharmacyInventory(Model model,@PathVariable String username){
        if (inventoryController.Authentication(username)) return "redirect:/login?loginagain";
        Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(username);
        String profilePicture = "/images/" + pharmacy.getProfilepicture();
        Map<String, Object> pharmacyDict = modelMapperUtil.mapFieldsToGetters(pharmacy, pharmacyService.getFields());
        pharmacyDict.put("profilepic",profilePicture);
        model.addAttribute("pharmacy",pharmacyDict);
        model.addAttribute("age", Period.between(pharmacy.getDOB(), LocalDate.now()).getYears());
        return "Pharmacy/pharmacy-inventory-add";
    }


    // Add Inventory Item
    @PostMapping("/pharmacy/{username}/inventory/addItem")
    public String addInventory(
            @RequestParam int inventoryID,
            @RequestParam Medicine medID,
            @RequestParam long price,
            @RequestParam LocalDate expiryDate,
            @RequestParam int quantityInStock,
            Model model,
            @PathVariable String username) {

        if (Authentication(username)) return "redirect:/login?loginagain";

        // Create a new Inventory item with auto-incremented InventoryID
        Inventory inventory = new Inventory(inventoryID, medID.getMedID(),price,expiryDate,quantityInStock);
        inventory.setInventoryID(inventoryIDCounter++);
        inventory.setMedicine(medID);
        inventory.setPrice(price);
        inventory.setExpiryDate(expiryDate);
        inventory.setQuantityInStock(quantityInStock);

        inventoryList.add(inventory);   // Add item to list

        // Update model for rendering
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("responseMessage", "Inventory item added successfully!");
        return "Pharmacy/pharmacy-inventory";
    }

    @PostMapping("/pharmacy/{username}/inventory/delete/{id}")
    public String deleteInventory(@PathVariable long id, Model model, @PathVariable String username) {
        if (Authentication(username)) return "redirect:/login?loginagain";
        // Remove the inventory item with the given ID
        boolean removed = inventoryList.removeIf(inventory -> inventory.getInventoryID() == id);

        // Set response message
        String message = removed ? "Inventory item deleted successfully!" : "Inventory item not found!";
        model.addAttribute("responseMessage", message);
        model.addAttribute("inventoryList", inventoryList); // Update list in the model
        return "Pharmacy/pharmacy-inventory"; // Redirect back to the inventory page
    }

    static boolean Authentication(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            if (authentication == null){
                System.out.println("Authentication is null");
            }
            else{
                System.out.println(username);
                System.out.println(authentication.getName());
            }
            return true;
        }
        return false;
    }

}
