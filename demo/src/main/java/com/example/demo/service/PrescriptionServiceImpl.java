package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.model.PrescriptionModel;
import com.example.demo.repo.PrescriptionDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService{

    private final PrescriptionDao prescriptionDao;

    @SuppressWarnings("serial")
    public static class ResourceNotFound extends RuntimeException {
        public ResourceNotFound(String message) {
            super(message);
        }
    }

    @Override
    public PrescriptionModel createPrescription(PrescriptionModel prescriptionModel) {
        //PrescriptionModel prescriptionModel=PrescriptionMapper.mapToPrescriptionModel(prescriptionModel);
        PrescriptionModel savedPrescription=prescriptionDao.save(prescriptionModel);
        return savedPrescription;//PrescriptionMapper.mapToPrescriptionModel(savedPrescription);
        
    }

    @Override
    public PrescriptionModel getPrescriptionById(Long presid) {
        PrescriptionModel prescriptionModel=prescriptionDao.findById(presid)
            .orElseThrow(()->
              new ResourceNotFound("the precription does does exist"));
        return prescriptionModel;//PrescriptionMapper.mapToPrescriptionModel(prescriptionModel);
    
    }

    @Override
    public List<PrescriptionModel> getAllPrescription() {
        List<PrescriptionModel> prescriptionModels=prescriptionDao.findAll();
        return prescriptionModels;//prescriptionModels.stream().map((prescription)->PrescriptionMapper.mapToPrescriptionModel(prescription ))
            //.collect(Collectors.toList());
        
    }

    @Override
    public PrescriptionModel updatePrescription(Long presid, PrescriptionModel updatedPrescription) {
        PrescriptionModel prescription = prescriptionDao.findById(presid)
            .orElseThrow(() -> new ResourceNotFound("Prescription does not exist with id: " + presid));

    // Updating fields if they are not null
    if (updatedPrescription.getDoctorId() != null) {
        prescription.setDoctorId(updatedPrescription.getDoctorId());
    }
    if (updatedPrescription.getPatientId() != null) {
        prescription.setPatientId(updatedPrescription.getPatientId());
    }
    if (updatedPrescription.getMeds() != null) {
        prescription.setMeds(updatedPrescription.getMeds());
    }
    if (updatedPrescription.getDosage() != null) {
        prescription.setDosage(updatedPrescription.getDosage());
    }
    if (updatedPrescription.getDateIssued() != null) {
        prescription.setDateIssued(updatedPrescription.getDateIssued());
    }
    if (updatedPrescription.getDescription() != null) {
        prescription.setDescription(updatedPrescription.getDescription());
    }

    // Saving updated prescription
    PrescriptionModel updatedPrescriptionObj = prescriptionDao.save(prescription);

    // Returning the updated Model
    return updatedPrescriptionObj;//PrescriptionMapper.mapToPrescriptionModel(updatedPrescriptionObj);
    }

    @Override
    public void deletePrescription(Long presid) {
        PrescriptionModel prescription=prescriptionDao.findById(presid).orElseThrow(
            () -> new ResourceNotFound("prescription does not exist")
        );

        prescriptionDao.deleteById(presid);
    }
    
}
