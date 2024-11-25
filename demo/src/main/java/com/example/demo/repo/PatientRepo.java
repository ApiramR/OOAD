package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Patient;

@Repository
public interface PatientRepo extends JpaRepository<Patient , Long>{
    Patient findByUsername(String Username);
    Patient findByUsernameAndTyp(String username,String typ);
    Patient findByEmailAndTyp(String email,String typ);
    Patient findByPnoAndTyp(String pno,String typ);
}
