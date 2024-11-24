package com.example.demo.service;

import java.util.List;

import com.example.demo.model.PrescriptionModel;

public interface  PrescriptionService {
    PrescriptionModel createPrescription(PrescriptionModel prescriptionModel);

    PrescriptionModel getPrescriptionById(Long appId);

    List<PrescriptionModel> getAllPrescription();

    PrescriptionModel updatePrescription(Long presid,PrescriptionModel updatedPrescription);

    void deletePrescription(Long presid);
}
