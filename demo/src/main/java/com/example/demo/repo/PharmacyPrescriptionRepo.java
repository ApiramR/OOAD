package com.example.demo.repo;

import com.example.demo.model.PharmacyPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyPrescriptionRepo extends JpaRepository<PharmacyPrescription, Long> {

    List<PharmacyPrescription> findByPharmacy_PharmacyIDAndIsReady(Long pharmacyID, Boolean isReady);

    List<PharmacyPrescription> findByPharmacy_PharmacyID(Long pharmacyID);

    List<PharmacyPrescription> findByIsReady(Boolean isReady);

    List<PharmacyPrescription> findByIsCompleted(Boolean isCompleted);

    @Query("SELECT COUNT(p) FROM PharmacyPrescription p WHERE p.isReady = true")
    static long countIsReadyTrue() {
        return 0;
    }

    @Query("SELECT COUNT(p) FROM PharmacyPrescription p WHERE p.isCompleted = true")
    static long countIsCompletedTrue() {
        return 0;
    }
}


