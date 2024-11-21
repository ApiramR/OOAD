package com.example.demo.service;
import com.example.demo.model.Doctor;
import com.example.demo.repo.DoctorRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo rep;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validate(String username, String password){
        Doctor doctor = rep.findByUsername(username);
        if (doctor != null && passwordEncoder.matches(password,doctor.getPassword())){
            return true;
        }
        return false;
    }

    public void addDoctor(Doctor doc){
        rep.save(doc);
    }
    
    public void updateDoctor(Doctor doc){
        rep.save(doc);
    }

    public Doctor getDoctor(int DocID){
        return rep.findById(DocID).orElse(new Doctor());
    } 
    public void deleteDoctor(int DocID){
        rep.deleteById(DocID);
    }
    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Doctor> entityType =  entityManager.getMetamodel().entity(Doctor.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Doctor, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }

}
