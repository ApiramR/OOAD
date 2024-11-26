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

    

    // public Prescription updatePrescription(Long presId, Prescription updatedPrescription) {
    //     // Fetch the existing prescription
    //     Prescription existingPrescription = prescriptionRepository.findById(presId)
    //             .orElseThrow(() -> new RuntimeException("Prescription not found"));

    //     // Check and set the patient reference if provided
    //     if (updatedPrescription.getPatient() != null) {
    //         // If the patient is provided and exists in the database, set it
    //         Patient patient = updatedPrescription.getPatient();
    //         if (patient.getId() != null) {
    //             patientRepository.findById(patient.getId())
    //                     .orElseThrow(() -> new RuntimeException("Patient not found for ID: " + patient.getId()));
    //         }
    //         existingPrescription.setPatient(patient);
    //     }

    //     // Update other fields
    //     existingPrescription.setMeds(updatedPrescription.getMeds());
    //     existingPrescription.setDosage(updatedPrescription.getDosage());
    //     existingPrescription.setDescription(updatedPrescription.getDescription());
    //     existingPrescription.setDateIssued(updatedPrescription.getDateIssued());

    //     // Save and return the updated prescription
    //     return prescriptionRepository.save(existingPrescription);
    // }
    
}
