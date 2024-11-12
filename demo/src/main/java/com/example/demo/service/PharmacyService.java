package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Pharmacy;
import com.example.demo.repo.PharmacyRepo;


public class PharmacyService {
    @Autowired
    PharmacyRepo rep;

    public void addPatient(Pharmacy pharmacy){
        rep.save(pharmacy);
    }
    
    public void updatePharmacy(Pharmacy pharmacy){
        rep.save(pharmacy);
    }

    public Pharmacy getPharmacy(int pharmacyID){
        return rep.findById(pharmacyID).orElse(new Pharmacy());
    } 

    public void deletePharmacy(int pharmacyID){
        rep.deleteById(pharmacyID);
    }
}
