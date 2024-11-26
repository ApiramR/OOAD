package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Prescription;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription , Long>{
    
}
