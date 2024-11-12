package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Doctor;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer>{

}
