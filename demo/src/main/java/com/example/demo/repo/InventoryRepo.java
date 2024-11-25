package com.example.demo.repo;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    @Query("SELECT i.medicine FROM Inventory i WHERE i.inventoryID = :inventoryId")
    Medicine findMedicineByInventoryId(@Param("inventoryId") Long inventoryId);

    @Query("SELECT i FROM Inventory i WHERE i.medicine.medID = :medicineId")
    List<Inventory> findInventoriesByMedicineId(@Param("medicineId") Long medicineId);
    
}

 */

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    @Query("SELECT i FROM Inventory i WHERE i.pharmacy.id = :pharmacyId")
    List<Inventory> findByPharmacyId(@Param("pharmacyId") Long pharmacyId);


    @Query("SELECT i FROM Inventory i WHERE i.medicine.medName LIKE %:query% OR i.medicine.strength LIKE %:query%")
    List<Inventory> findByMedicineNameContainingIgnoreCaseOrStrengthContainingIgnoreCase(@Param("query") String query1, @Param("query") String query2);

}
