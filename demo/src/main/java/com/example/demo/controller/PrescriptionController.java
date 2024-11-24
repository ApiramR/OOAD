package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.model.PrescriptionModel;
import com.example.demo.service.PrescriptionService;

import lombok.AllArgsConstructor;



@RestController
@AllArgsConstructor
@RequestMapping("api/prescriptions")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService ;

    @PostMapping 
    public ResponseEntity<PrescriptionModel> createPrescription(@RequestBody PrescriptionModel prescriptionModel){
        PrescriptionModel savedPrescription= prescriptionService.createPrescription(prescriptionModel);
        return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PrescriptionModel> getPrescriptionById(@PathVariable("id") Long presid){
        PrescriptionModel prescriptionModel =prescriptionService.getPrescriptionById(presid);
            return ResponseEntity.ok(prescriptionModel);

    }

    @GetMapping
    public ResponseEntity<List<PrescriptionModel>> getAllPrecription(){
        List<PrescriptionModel> prescription=prescriptionService.getAllPrescription();
        return ResponseEntity.ok(prescription);
    }

    @PutMapping("{id}")
    public ResponseEntity<PrescriptionModel> updatePrescription(@PathVariable("id") Long presid,@RequestBody PrescriptionModel updatedPrescription){
        System.out.println("update in progress");
        PrescriptionModel prescriptionModel= prescriptionService.updatePrescription(presid, updatedPrescription);
        return ResponseEntity.ok(prescriptionModel);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable("id") Long presid){
        prescriptionService.deletePrescription(presid);
        return ResponseEntity.ok("deletion successful");
    }
}
