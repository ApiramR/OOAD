package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Prescription;
import com.example.demo.repo.PrescriptionRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class PrescriptionService{

    @Autowired
    private PrescriptionRepo rep;
    
    public Prescription addPrescription(Prescription prescription){
        System.out.println("Yo whats going on!");
        return rep.save(prescription);
    }
    
    // public Prescription updatePrescription(Prescription prescription){
    //     return rep.save(prescription);
    // }


    // public Prescription updatePrescription(Prescription prescription) {
    //     // Check if the prescription ID exists
    //     if (!prescriptionRepo.existsById(prescription.getPresID())) {
    //         throw new RuntimeException("Prescription not found with ID: " + prescription.getPresID());
    //     }

    //     // Save and return the updated prescription
    //     return prescriptionRepo.save(prescription);
    // }

    public Prescription getPrescriptionByID(Long PresID){
        return rep.findById(PresID).orElse(new Prescription());
    } 
    public void deletePrescription(Long PresID){
        rep.deleteById(PresID);
    }

    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Prescription> entityType =  entityManager.getMetamodel().entity(Prescription.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Prescription, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }

    public long countAllPrescriptions() {
        return rep.count();
    }


}
