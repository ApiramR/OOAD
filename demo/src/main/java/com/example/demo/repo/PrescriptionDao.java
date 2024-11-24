package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PrescriptionModel;

public interface PrescriptionDao extends JpaRepository<PrescriptionModel, Long>{
    
}
