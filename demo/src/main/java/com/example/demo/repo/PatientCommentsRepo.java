package com.example.demo.repo;
import com.example.demo.model.PatientComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientCommentsRepo extends JpaRepository<PatientComments, Long> {

}