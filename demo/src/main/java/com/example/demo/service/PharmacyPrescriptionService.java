package com.example.demo.service;


import com.example.demo.model.PharmacyPrescription;
import com.example.demo.repo.PharmacyPrescriptionRepo;
import com.example.demo.repo.PrescriptionRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyPrescriptionService {

    @Autowired
    private static PrescriptionRepo prescriptionRepo;
    private final PharmacyPrescriptionRepo pharmacyPrescriptionRepo;


    public static long countAllPrescriptionsPrepare() {
            return prescriptionRepo.count();
    }

    public static long countAllPrescriptionsReady() {
        return prescriptionRepo.count();
    }

    public static long countAllPrescriptionsOrder() {
        return prescriptionRepo.count();
    }

    private final PharmacyPrescriptionRepo repository;

    public List<PharmacyPrescription> getAllPrescriptions() {
        return pharmacyPrescriptionRepo.findAll();
    }


    public List<PharmacyPrescription> getReadyOrders(Long pharmacyID) {
        return repository.findByPharmacy_PharmacyIDAndIsReady(pharmacyID, true);
    }

    public void markOrderAsCompleted(Long id) {
        PharmacyPrescription pharmacyPrescription = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PharmacyPrescription not found!"));
        pharmacyPrescription.setIsCompleted(true);
        repository.save(pharmacyPrescription);
    }

    public List<PharmacyPrescription> getPrescriptionsByPharmacyID(Long pharmacyID) {
        return repository.findAllByPharmacyID(pharmacyID);
    }


    @Autowired
    public PharmacyPrescriptionService(PharmacyPrescriptionRepo repository, PharmacyPrescriptionRepo pharmacyPrescriptionRepo) {
        this.repository = repository;
        this.pharmacyPrescriptionRepo = pharmacyPrescriptionRepo;
    }

    public long countIsReadyTrue() {
        return repository.countIsReadyTrue()-repository.countIsCompletedTrue();
    }

    public long countIsCompletedTrue() {
        return repository.countIsCompletedTrue();
    }

    public List<PharmacyPrescription> getPrescriptionsByUsername(String username) {
        return repository.findAllByPharmacyUsername(username);
    }


}
