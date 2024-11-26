package com.example.demo.repo;


import com.example.demo.model.Medicine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicineRepo extends JpaRepository<Medicine, Long> {
    Medicine findBymedName(String medName);
    @Query("SELECT m FROM Medicine m WHERE m.medName LIKE %:query% OR m.medType LIKE %:query% OR m.manufacturer LIKE %:query%")
    List<Medicine> findByMedNameContainingIgnoreCaseOrMedTypeContainingIgnoreCaseOrManufacturerContainingIgnoreCase(@Param("query") String query1, @Param("query") String query2, @Param("query") String query3);

}
