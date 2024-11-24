package com.example.demo.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repo.InventoryRepo;
import com.example.demo.model.Inventory;


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
