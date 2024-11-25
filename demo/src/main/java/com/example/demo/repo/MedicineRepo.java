package com.example.demo.repo;


import com.example.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRepo extends JpaRepository<Medicine, Long> {

    List<Long> findMedIDByMedName(String medName);
    Medicine findByMedID(Long medID);

    @Query("SELECT m FROM Medicine m WHERE m.medName LIKE %:query% OR m.medType LIKE %:query% OR m.manufacturer LIKE %:query%")
    List<Medicine> findByMedNameContainingIgnoreCaseOrMedTypeContainingIgnoreCaseOrManufacturerContainingIgnoreCase(@Param("query") String query1, @Param("query") String query2, @Param("query") String query3);


}
