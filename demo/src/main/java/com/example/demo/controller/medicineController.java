package com.example.demo.controller;

import com.example.demo.model.Medicine;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/medicine")
public class medicineController {

    @Autowired
    private MedicineService MedicineService;
    @Autowired
    private MedicineService medicineService;

    // Add a new medicine
    @PostMapping("/add")
    public ResponseEntity<Medicine> addMedicine(@RequestBody Medicine medicine) {
        Medicine addedMedicine = MedicineService.addMedicine(medicine);
        return ResponseEntity.ok(addedMedicine);
    }

    // Update an existing medicine
    @PutMapping("/update/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine updatedMedicine) {
        Medicine existingMedicine = MedicineService.findById(id.intValue());
        existingMedicine.setMedName(updatedMedicine.getMedName());
        existingMedicine.setMedType(updatedMedicine.getMedType());
        existingMedicine.setStrength(updatedMedicine.getStrength());
        existingMedicine.setManufacturer(updatedMedicine.getManufacturer());
        existingMedicine.setInventories(updatedMedicine.getInventories());

        Medicine savedMedicine = MedicineService.addMedicine(existingMedicine);
        return ResponseEntity.ok(savedMedicine);
    }

    // Delete a medicine by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable Long id) {
        MedicineService.deleteMedicine(id);
        return ResponseEntity.ok("Medicine with ID " + id + " has been deleted.");
    }

    // Show all medicines
    @GetMapping("/all")
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        List<Medicine> medicines = MedicineService.getAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    // Show medicine by ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = MedicineService.findById(id.intValue());
        return ResponseEntity.ok(medicine);
    }



    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchMedicines(@RequestParam String query) {
        try {
            // Fetch medicines based on the search query
            List<Medicine> medicines = medicineService.searchMedicines(query);

            // Create a unique response mapped by medID
            Map<Long, Map<String, Object>> uniqueMedicineMap = new HashMap<>();

            for (Medicine item : medicines) {
                if (!uniqueMedicineMap.containsKey(item.getMedID())) {
                    Map<String, Object> medicineDetails = new HashMap<>();
                    medicineDetails.put("medID", item.getMedID());
                    medicineDetails.put("medicineName", item.getMedName());
                    medicineDetails.put("medType", item.getMedType());
                    medicineDetails.put("strength", item.getStrength());
                    medicineDetails.put("manufacturer", item.getManufacturer());
                    uniqueMedicineMap.put(item.getMedID(), medicineDetails);
                }
            }

            // Convert the unique map values to a list
            List<Map<String, Object>> response = new ArrayList<>(uniqueMedicineMap.values());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMedicineItems() {
        try {
            long itemCount = medicineService.countAllMedicine();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }





}
