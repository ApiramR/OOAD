package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Pharmacy;

import java.util.List;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy , Integer>{
    Pharmacy findByUsername(String Username);
    Pharmacy findByUsernameAndTyp(String username,String typ);
    Pharmacy findByEmailAndTyp(String email,String typ);
    Pharmacy findByPnoAndTyp(String pno,String typ);

    List<Pharmacy> findByInventoryid(Long inventoryID);
}
