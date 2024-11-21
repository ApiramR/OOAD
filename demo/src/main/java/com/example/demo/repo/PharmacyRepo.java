package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Pharmacy;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy , Integer>{
    Pharmacy findByUsername(String Username);
}
