package com.example.demo.controller.pharmacy;

import com.example.demo.model.Inventory;
import com.example.demo.model.PharmacyPrescription;
import com.example.demo.model.Prescription;
import com.example.demo.service.PharmacyPrescriptionService;
import com.example.demo.service.PrescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/prescriptions")
public class pharmacyPrescriptionController {

    private final PharmacyPrescriptionService pharmacyPrescriptionService;
    private final PrescriptionService prescriptionService;

    public ResponseEntity<Long> countPrescription() {
        try {
            long itemCount = prescriptionService.countAllPrescriptions();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }


    // Constructor-based injection (recommended)
    @Autowired
    public pharmacyPrescriptionController(PharmacyPrescriptionService pharmacyPrescriptionService, PrescriptionService prescriptionService) {
        this.pharmacyPrescriptionService = pharmacyPrescriptionService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/ready/count")
    public long getIsReadyCount() {
        // Call the non-static method using the injected service instance
        return pharmacyPrescriptionService.countIsReadyTrue();
    }

    @GetMapping("/completed/count")
    public long getIsCompletedCount() {
        return pharmacyPrescriptionService.countIsCompletedTrue();
    }

    @GetMapping("/prepare/count")
    public long ToPrepareCount() {
        // Call the non-static method using the injected service instance
        return prescriptionService.countAllPrescriptions() - (pharmacyPrescriptionService.countIsReadyTrue()+pharmacyPrescriptionService.countIsCompletedTrue());
    }

    @GetMapping("/{username}/prescriptions")
    public ResponseEntity<List<PharmacyPrescription>> getPrescriptionsByUsername(@PathVariable String username) {
        List<PharmacyPrescription> prescriptions = pharmacyPrescriptionService.getPrescriptionsByUsername(username);
        return ResponseEntity.ok(prescriptions);
    }




}
