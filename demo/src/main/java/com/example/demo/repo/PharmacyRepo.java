package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Pharmacy;


@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy , Long>{
    Pharmacy findByUsername(String Username);
    Pharmacy findByUsernameAndTyp(String username,String typ);
    Pharmacy findByEmailAndTyp(String email,String typ);
    Pharmacy findByPnoAndTyp(String pno,String typ);
}
