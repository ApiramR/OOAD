package com.example.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Prescription;
import com.example.demo.service.PrescriptionService;

import lombok.AllArgsConstructor;



@RestController
@AllArgsConstructor
@RequestMapping("api/prescriptions")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService ;

    @PostMapping 
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescriptionModel){
        Prescription savedPrescription= prescriptionService.addPrescription(prescriptionModel);
        return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Long presid){
        Prescription prescriptionModel =prescriptionService.getPrescriptionByID(presid);
            return ResponseEntity.ok(prescriptionModel);

    }


    @PutMapping("{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable("id") Long presid,@RequestBody Prescription updatedPrescription){
        System.out.println("update in progress");
        Prescription prescriptionModel= prescriptionService.updatePrescription(updatedPrescription);
        return ResponseEntity.ok(prescriptionModel);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable("id") Long presid){
        prescriptionService.deletePrescription(presid);
        return ResponseEntity.ok("deletion successful");
    }
}
