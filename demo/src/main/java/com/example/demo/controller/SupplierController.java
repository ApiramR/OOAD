package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Pharmacy;
import com.example.demo.model.Supplier;
import com.example.demo.service.MedicineService;
import com.example.demo.service.ModelMapperUtil;
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

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    // Counter for auto-incrementing InventoryID
    private long inventoryIDCounter = 1;

    private final List<Inventory> inventoryList = new ArrayList<>();

    @RequestMapping(value="/supplier",method = RequestMethod.GET)
    String PharmacyDashboard(Model model){
        if (Auth(model)) return "redirect:/login";
        return "supplierInventory";
    }

    @PostMapping("/add")
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
        Inventory inventory = new Inventory();
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

    // @PutMapping("/update/{id}")
    // public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
    //     try {
    //         // Fetch Inventory by ID
    //         Inventory existingInventory = suppInventoryService.findById(id);
    //         if (existingInventory == null) {
    //             return ResponseEntity.badRequest().body("Inventory not found");
    //         }

    //         // Parse request data and update inventory details
    //         Long medID = Long.valueOf(request.get("medID").toString());
    //         Long price = Long.valueOf(request.get("price").toString());
    //         String expiryDate = request.get("expiryDate").toString();
    //         Integer quantityInStock = Integer.valueOf(request.get("quantityInStock").toString());

    //         Medicine medicine = medicineService.getMedicineByMedID(medID);
    //         if (medicine == null) {
    //             return ResponseEntity.badRequest().body("Invalid Medicine ID");
    //         }

    //         existingInventory.setMedicine(medicine);
    //         existingInventory.setPrice(price);
    //         existingInventory.setExpiryDate(LocalDate.parse(expiryDate));
    //         existingInventory.setQuantityInStock(quantityInStock);

    //         suppInventoryService.addSuppInventory(existingInventory); // Save updated inventory

    //         return ResponseEntity.ok("Inventory updated successfully.");
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    //     }
    // }

    // @DeleteMapping("/delete/{id}")
    // public ResponseEntity<String> deleteSuppInventory(@PathVariable Long id) {
    //     suppInventoryService.deleteSuppInventory(id);
    //     return ResponseEntity.ok("Inventory with ID " + id + " has been deleted.");
    // }


    // @GetMapping("/all")
    // public ResponseEntity<List<Map<String, Object>>> getAllInventory() {
    //     List<Inventory> inventoryList = suppInventoryService.getAllInventory();
    //     List<Map<String, Object>> response = inventoryList.stream().map(item -> {
    //         Map<String, Object> inventoryMap = new HashMap<>();
    //         inventoryMap.put("inventoryID", item.getInventoryID());
    //         inventoryMap.put("medID", item.getMedicine() != null ? item.getMedicine().getMedID() : null);
    //         inventoryMap.put("medicineName", item.getMedicine() != null ? item.getMedicine().getMedName() : "Unknown");
    //         inventoryMap.put("strength", item.getMedicine() != null ? item.getMedicine().getStrength() : "Unknown");
    //         inventoryMap.put("price", item.getPrice());
    //         inventoryMap.put("expiryDate", item.getExpiryDate() != null ? item.getExpiryDate().toString() : null);
    //         inventoryMap.put("quantityInStock", item.getQuantityInStock());
    //         return inventoryMap;
    //     }).toList();

    //     return ResponseEntity.ok(response);
    // }

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

    private boolean Auth(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authenticated User roles: " + authentication.getAuthorities());
        }
        if (authentication != null){
            String username = authentication.getName();
            Supplier supplier = supplierService.getSupplierByUsername(username);
            String profilepicture = "/images/" + supplier.getProfilepicture();

            String[] fields = supplierService.getFields();
            Map<String, Object> supplierDict = modelMapperUtil.mapFieldsToGetters(supplier, fields);
            supplierDict.put("profilepic",profilepicture);
            model.addAttribute("pharmacy",supplierDict);
        }
        else{
            return true;
        }
        return false;
    }

}
