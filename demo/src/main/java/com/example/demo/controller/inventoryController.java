package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Pharmacy;
import com.example.demo.service.InventoryService;
import com.example.demo.service.MedicineService;

import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/api/inventory")
public class inventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PharmacyService pharmacyService;


    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody Map<String, Object> request) {
        try {

            // Parse request data
            Long medID = Long.valueOf(request.get("medID").toString());
            long price = Long.parseLong(request.get("price").toString());
            String expiryDate = request.get("expiryDate").toString();
            Integer quantityInStock = Integer.valueOf(request.get("quantityInStock").toString());
            String pharmacyUsername = request.get("pharmacyUsername").toString();

            // Fetch Medicine by medID
            Medicine medicine = medicineService.getMedicineByMedID(medID);
            if (medicine == null) {
                return ResponseEntity.badRequest().body("Invalid Medicine ID");
            }

            // Fetch Pharmacy by username
            Pharmacy pharmacy = pharmacyService.getPharmacyByUsername(pharmacyUsername);

            // Create and save Inventory
            Inventory inventory = new Inventory();
            inventory.setMedicine(medicine);
            inventory.setPharmacy(pharmacy);
            inventory.setPrice(price);
            inventory.setExpiryDate(LocalDate.parse(expiryDate));
            inventory.setQuantityInStock(quantityInStock);

            inventoryService.addInventory(inventory);

            return ResponseEntity.ok("Inventory added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            // Fetch Inventory by ID
            Inventory existingInventory = inventoryService.findById(id);
            if (existingInventory == null) {
                return ResponseEntity.badRequest().body("Inventory not found");
            }

            // Parse request data and update inventory details
            Long medID = Long.valueOf(request.get("medID").toString());
            Long price = Long.valueOf(request.get("price").toString());
            String expiryDate = request.get("expiryDate").toString();
            Integer quantityInStock = Integer.valueOf(request.get("quantityInStock").toString());

            Medicine medicine = medicineService.getMedicineByMedID(medID);
            if (medicine == null) {
                return ResponseEntity.badRequest().body("Invalid Medicine ID");
            }

            existingInventory.setMedicine(medicine);
            existingInventory.setPrice(price);
            existingInventory.setExpiryDate(LocalDate.parse(expiryDate));
            existingInventory.setQuantityInStock(quantityInStock);

            inventoryService.addInventory(existingInventory); // Save updated inventory

            return ResponseEntity.ok("Inventory updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("Inventory with ID " + id + " has been deleted.");
    }


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


    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchInventory(@RequestParam String query) {
        try {
            List<Inventory> inventoryList = inventoryService.searchInventory(query);
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countInventoryItems() {
        try {
            long itemCount = inventoryService.countAllInventory();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
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

