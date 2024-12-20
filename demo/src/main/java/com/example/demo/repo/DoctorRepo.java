package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Doctor;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long>{
    Doctor findByUsername(String username);
    Doctor findByUsernameAndTyp(String username,String typ);
    Doctor findByEmailAndTyp(String email,String typ);
    Doctor findByPnoAndTyp(String pno,String typ);
}
