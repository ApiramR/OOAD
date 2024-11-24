package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Patient;
import com.example.demo.repo.PatientRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class PatientService {
    @Autowired
    PatientRepo rep;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validate(String username, String password){
        Patient patient = rep.findByUsername(username);
        if (patient != null && passwordEncoder.matches(password,patient.getPassword())){
            return true;
        }
        return false;
    }
    public String addPatient(Patient patient){
        if (rep.findByUsernameAndTyp(patient.getUsername(),patient.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(patient.getEmail(),patient.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(patient.getPno(),patient.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(patient);
        return "success";
    }
    
    public Patient updatePatient(Patient patient){
        return rep.save(patient);
    }

    public Patient getPatientByID(int patientID){
        return rep.findById(patientID).orElse(new Patient());
    } 
    public Patient getPatientByUsername(String username){
        return rep.findByUsername(username);
    }
    public void deletePatient(int patientID){
        rep.deleteById(patientID);
    }
    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Patient> entityType =  entityManager.getMetamodel().entity(Patient.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Patient, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
}
