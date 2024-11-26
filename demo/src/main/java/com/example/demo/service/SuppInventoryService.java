package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.SuppInventory;
import com.example.demo.repo.SuppInventoryRepo;

@Service
public class SuppInventoryService {

    @Autowired
    private SuppInventoryRepo suppInventoryRepo;


    public SuppInventory addSuppInventory(SuppInventory suppInventory) {
        return suppInventoryRepo.save(suppInventory); // Save Inventory with valid Medicine
    }


    public SuppInventory updateSuppInventory(SuppInventory suppInventory) {
        return suppInventoryRepo.save(suppInventory);
    }

    public void deleteSuppInventory(Long id) {
        suppInventoryRepo.deleteById(id);
    }

    public List<SuppInventory> getAllSuppInventory() {
        return suppInventoryRepo.findAll();
    }
}
