package com.example.demo.repo;


import com.example.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepo extends JpaRepository<Medicine, Long> {

    List<Long> findMedIDByMedName(String medName);


}
