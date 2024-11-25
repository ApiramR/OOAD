package com.example.demo.controller.pharmacy;

import com.example.demo.service.PharmacyPrescriptionService;
import com.example.demo.service.PrescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/prescriptions")
public class pharmacyPrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/prepare/count")
    public ResponseEntity<Long> countPreparedPrescriptions() {
        try {
            long itemCount = PharmacyPrescriptionService.countAllPrescriptionsPrepare();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

    @GetMapping("/ready/count")
    public ResponseEntity<Long> countReadyPrescriptions() {
        try {
            long itemCount = PharmacyPrescriptionService.countAllPrescriptionsReady();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

    @GetMapping("/order/count")
    public ResponseEntity<Long> countOrderPrescriptions() {
        try {
            long itemCount = PharmacyPrescriptionService.countAllPrescriptionsOrder();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

}
