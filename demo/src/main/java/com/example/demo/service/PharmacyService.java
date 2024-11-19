package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pharmacy;
import com.example.demo.repo.PharmacyRepo;

@Service
public class PharmacyService {
    @Autowired
    PharmacyRepo rep;

    public void addPharmacy(Pharmacy pharmacy){
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
