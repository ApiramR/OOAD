package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Pharmacy;

import com.example.demo.service.InventoryService;
import com.example.demo.service.MedicineService;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/*

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

} */

@RestController
@RequestMapping("/api/inventory")
public class inventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PharmacyService pharmacyService;


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        if (inventory.getMedicine() == null || inventory.getMedicine().getMedID() == null) {
            return ResponseEntity.badRequest().body(null); // Return error if medicine is null
        }

        // Fetch the Medicine object by ID
        Medicine medicine = medicineService.findById(inventory.getMedicine().getMedID());
        if (medicine == null) {
            return ResponseEntity.badRequest().body(null); // Return error if Medicine doesn't exist
        }

        // Set the fetched Medicine object to the Inventory
        inventory.setMedicine(medicine);

        // Save the Inventory
        Inventory addedInventory = inventoryService.addInventory(inventory);
        return ResponseEntity.ok(addedInventory);
    }



    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addInventory(
            @RequestParam(required = false) Long medID,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) Integer quantityInStock,
            @RequestParam(required = false) Long pharmacyID) {

        // Validate required parameters
        if (medID == null) {
            return ResponseEntity.badRequest().body("Error: Medicine ID (medID) is required.");
        }
        if (price == null) {
            return ResponseEntity.badRequest().body("Error: Price is required.");
        }
        if (expiryDate == null || expiryDate.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Expiry Date is required.");
        }
        if (quantityInStock == null) {
            return ResponseEntity.badRequest().body("Error: Quantity in Stock is required.");
        }
        if (pharmacyID == null) {
            return ResponseEntity.badRequest().body("Error: Pharmacy ID (pharmacyID) is required.");
        }

        try {
            // Fetch the Medicine object
            Medicine medicine = medicineService.findById(medID);

            // Fetch the Pharmacy object
            Pharmacy pharmacy = pharmacyService.findById(pharmacyID);

            // Create and populate Inventory object
            Inventory inventory = new Inventory();
            inventory.setMedicine(medicine);
            inventory.setPrice(price);
            inventory.setExpiryDate(LocalDate.parse(expiryDate));
            inventory.setQuantityInStock(quantityInStock);
            inventory.setPharmacy(pharmacy);

            // Save the Inventory
            Inventory addedInventory = inventoryService.addInventory(inventory);
            return ResponseEntity.ok("Inventory added successfully. ID: " + addedInventory.getInventoryID());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory updatedInventory) {
        Inventory inventory = inventoryService.findById(id);
        inventory.setPrice(updatedInventory.getPrice());
        inventory.setExpiryDate(updatedInventory.getExpiryDate());
        inventory.setQuantityInStock(updatedInventory.getQuantityInStock());
        inventory.setMedicine(updatedInventory.getMedicine());
        Inventory savedInventory = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(savedInventory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("Inventory with ID " + id + " has been deleted.");
    }
/*
    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryList);
    }*/

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        List<Map<String, Object>> response = inventoryList.stream().map(item -> {
            Map<String, Object> inventoryMap = new HashMap<>();
            inventoryMap.put("inventoryID", item.getInventoryID());
            inventoryMap.put("medID", item.getMedicine() != null ? item.getMedicine().getMedID() : null);
            inventoryMap.put("medicineName", item.getMedicine() != null ? item.getMedicine().getMedName() : "Unknown");
            inventoryMap.put("strength", item.getMedicine() != null ? item.getMedicine().getStrength() : "Unknown");
            inventoryMap.put("price", item.getPrice());
            inventoryMap.put("expiryDate", item.getExpiryDate() != null ? item.getExpiryDate().toString() : null);
            inventoryMap.put("quantityInStock", item.getQuantityInStock());
            return inventoryMap;
        }).toList();

        return ResponseEntity.ok(response);
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

