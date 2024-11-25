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

    public PharmacyPrescriptionService(PharmacyPrescriptionRepo repository) {
        this.repository = repository;
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
}
