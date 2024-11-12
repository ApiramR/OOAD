package com.example.demo.service;
import com.example.demo.model.Doctor;
import com.example.demo.repo.DoctorRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    @Autowired
    DoctorRepo rep;

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
}
