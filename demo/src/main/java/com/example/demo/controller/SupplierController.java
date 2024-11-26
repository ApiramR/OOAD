package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.SuppInventory;
import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Supplier;
import com.example.demo.service.MedicineService;
import com.example.demo.service.SuppInventoryService;
import com.example.demo.service.SupplierService;

@RestController
@RequestMapping("/inventory")
public class SupplierController {

    @Autowired
    private SuppInventoryService suppInventoryService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody Map<String, Object> request) {
        try {

            // Parse request data
            Long medID = Long.valueOf(request.get("medID").toString());
            long price = Long.parseLong(request.get("price").toString());
            String expiryDate = request.get("expiryDate").toString();
            Integer quantityInStock = Integer.valueOf(request.get("quantityInStock").toString());
            String supplierUsername = request.get("pharmacyUsername").toString();

            // Fetch Medicine by medID
            // Medicine medicine = medicineService.getMedicineByMedID(medID);
            // if (medicine == null) {
            //     return ResponseEntity.badRequest().body("Invalid Medicine ID");
            // }

            SupplierService supplierService;

            // Fetch Pharmacy by username
            Supplier supplier = supplierService.getSupplierByUsername(supplierUsername);

            // Create and save Inventory
            SuppInventory suppInventory = new SuppInventory();
            //SuppInventory.setMedicine(medicine);
            // SuppInventory.setSupplier(supplier);
            // SuppInventory.setPrice(price);
            // SuppInventory.setExpiryDate(LocalDate.parse(expiryDate));
            // SuppInventory.setQuantityInStock(quantityInStock);

            // suppInventoryService.addInventory(suppInventory);

            return ResponseEntity.ok("Inventory added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            // Fetch Inventory by ID
            Inventory existingInventory = suppInventoryService.findById(id);
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

            suppInventoryService.addSuppInventory(existingInventory); // Save updated inventory

            return ResponseEntity.ok("Inventory updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSuppInventory(@PathVariable Long id) {
        suppInventoryService.deleteSuppInventory(id);
        return ResponseEntity.ok("Inventory with ID " + id + " has been deleted.");
    }


    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllInventory() {
        List<Inventory> inventoryList = suppInventoryService.getAllInventory();
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

}
