package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Doctor;

public interface DoctorFetchDao extends JpaRepository<Doctor, Long>{
    
}
