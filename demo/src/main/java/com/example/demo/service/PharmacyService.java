package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pharmacy;
import com.example.demo.repo.PharmacyRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class PharmacyService {
    @Autowired
    PharmacyRepo rep;

    public String addPharmacy(Pharmacy pharmacy){
        if (rep.findByUsernameAndTyp(pharmacy.getUsername(),pharmacy.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(pharmacy.getEmail(),pharmacy.getTyp()) == null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(pharmacy.getPno(),pharmacy.getTyp()) == null){
            return "Pno already taken";
        }
        rep.save(pharmacy);
        return "success";
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
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validate(String username, String password){
        Pharmacy pharmacy = rep.findByUsername(username);
        if (pharmacy != null && passwordEncoder.matches(password,pharmacy.getPassword())){
            return true;
        }
        return false;
    }

    public String[] getFields(){
        EntityType<Pharmacy> entityType =  entityManager.getMetamodel().entity(Pharmacy.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Pharmacy, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
}
