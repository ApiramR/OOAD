package com.example.demo.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private PharmacyRepo pharmacyRepo;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    public String addPharmacy(Pharmacy pharmacy){
        if (rep.findByUsernameAndTyp(pharmacy.getUsername(),pharmacy.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(pharmacy.getEmail(),pharmacy.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(pharmacy.getPno(),pharmacy.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(pharmacy);
        return "success";
    }





    public Pharmacy getPharmacyByID(Long pharmacyID){
        return rep.findById(pharmacyID).orElse(new Pharmacy());
    } 
    public Pharmacy getPharmacyByUsername(String username){
        return rep.findByUsername(username);
    }
    public void deletePharmacy(Long pharmacyID){
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


    public Pharmacy findById(Long id) {
        return pharmacyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
    }


    public Boolean updatePharmacy(Map<String, String> formData, Pharmacy pharmacy) {
            try {
                for (Map.Entry<String, String> entry : formData.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue();

                    if (fieldValue == null || fieldValue.isEmpty()) {
                        continue;
                    }

                    // Handle password encoding
                    if ("Password".equalsIgnoreCase(fieldName)) {
                        fieldValue = passwordEncoder.encode(fieldValue);
                    }

                    // Handle dynamic field updates using reflection
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setter = Pharmacy.class.getMethod(methodName, String.class);
                    setter.invoke(pharmacy, fieldValue);
                }
                // Save the updated pharmacy
                pharmacyRepo.save(pharmacy);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


