package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SuppInventory;

public interface SuppInventoryRepo extends JpaRepository<SuppInventory, Long>{

    
    
}
