package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Supplier;
import com.example.demo.repo.SupplierRepo;


public class SupplierService {
    @Autowired
    SupplierRepo rep;

    public void addSupplier(Supplier supplier){
        rep.save(supplier);
    }
    
    public void updateSupplier(Supplier supplier){
        rep.save(supplier);
    }

    public Supplier getSupplier(int supplierID){
        return rep.findById(supplierID).orElse(new Supplier());
    } 

    public void deleteSupplier(int supplierID){
        rep.deleteById(supplierID);
    }
}
