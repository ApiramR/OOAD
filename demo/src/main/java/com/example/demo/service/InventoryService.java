package com.example.demo.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repo.InventoryRepo;
import com.example.demo.model.Inventory;

/*
@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepo.save(inventory);
    }

    public List<Inventory> findInventoriesByMedId(Long medicineId) {
        return inventoryRepo.findInventoriesByMedicineId(medicineId);
    }

}
*/

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepo.save(inventory); // Save Inventory with valid Medicine
    }


    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepo.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepo.deleteById(id);
    }

    public Inventory findById(Long id) {
        return inventoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepo.findAll();
    }

    public List<Inventory> searchInventory(String query) {
        return inventoryRepo.findByMedicineNameContainingIgnoreCaseOrStrengthContainingIgnoreCase(query, query);
    }

    public long countAllInventory() {
        return inventoryRepo.count();
    }


}
