package com.example.demo.repo;

import com.example.demo.model.PharmacyPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyPrescriptionRepo extends JpaRepository<PharmacyPrescription, Long> {

    List<PharmacyPrescription> findByPharmacy_PharmacyIDAndIsReady(Long pharmacyID, Boolean isReady);

    List<PharmacyPrescription> findByPharmacy_PharmacyID(Long pharmacyID);

    List<PharmacyPrescription> findByIsReady(Boolean isReady);

    List<PharmacyPrescription> findByIsCompleted(Boolean isCompleted);

    @Query("SELECT COUNT(p) FROM PharmacyPrescription p WHERE p.isReady = true")
    long countIsReadyTrue();

    @Query("SELECT COUNT(p) FROM PharmacyPrescription p WHERE p.isCompleted = true")
    long countIsCompletedTrue();

    @Query("SELECT p FROM PharmacyPrescription  p WHERE p.pharmacy.pharmacyID = :pharmacyID")
    List<PharmacyPrescription> findAllByPharmacyID(@Param("pharmacyID") Long pharmacyID);

    @Query("SELECT p FROM PharmacyPrescription p WHERE p.pharmacy.username = :username")
    List<PharmacyPrescription> findAllByPharmacyUsername(@Param("username") String username);

}


