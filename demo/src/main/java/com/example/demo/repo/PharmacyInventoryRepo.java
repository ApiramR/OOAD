package com.example.demo.repo;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Pharmacy;

@Repository
public interface PharmacyInventoryRepo extends JpaRepository<Pharmacy , Integer>{
    Pharmacy findByPharmacyInventories(String inventory);
    Pharmacy findByPharmacyInventoriesAndTyp(String inventory,String typ);
}

