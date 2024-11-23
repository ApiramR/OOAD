package com.example.demo.controller;

import com.example.demo.model.Medicine;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine")
public class medicineController {

    @Autowired
    private MedicineService MedicineService;

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
}
