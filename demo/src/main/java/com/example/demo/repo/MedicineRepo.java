package com.example.demo.repo;


import com.example.demo.model.Medicine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepo extends JpaRepository<Medicine, Long> {
    Medicine findBymedName(String medName);


}
