package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Supplier;
import com.example.demo.repo.SupplierRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class SupplierService {
    @Autowired
    SupplierRepo rep;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validate(String username, String password){
        Supplier supplier = rep.findByUsername(username);
        if (supplier != null && passwordEncoder.matches(password,supplier.getPassword())){
            return true;
        }
        return false;
    }

    public String addSupplier(Supplier supplier){
        if (rep.findByUsernameAndTyp(supplier.getUsername(),supplier.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(supplier.getEmail(),supplier.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(supplier.getPno(),supplier.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(supplier);
        return "success";
    }
    
    public void updateSupplier(Supplier supplier){
        rep.save(supplier);
    }

    public Supplier getSupplierByID(int supplierID){
        return rep.findById(supplierID).orElse(new Supplier());
    } 
    public Supplier getSupplierByUsername(String username){
        return rep.findByUsername(username);
    }

    public void deleteSupplier(int supplierID){
        rep.deleteById(supplierID);
    }
    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Supplier> entityType =  entityManager.getMetamodel().entity(Supplier.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Supplier, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
}
