package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.demo.model.Supplier;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier , Integer>{
    Supplier findByUsername(String Username);
    Supplier findByUsernameAndTyp(String username,String typ);
    Supplier findByEmailAndTyp(String email,String typ);
    Supplier findByPnoAndTyp(String pno,String typ);
}
