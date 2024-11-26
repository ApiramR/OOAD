package com.example.demo.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.PatientComments;
import com.example.demo.model.Report;
import com.example.demo.repo.PatientCommentsRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class PatientCommentsService {
    @Autowired
    PatientCommentsRepo rep;
    public String addComments(String content,Patient patient,Doctor doctor){
        PatientComments comments = new PatientComments();
        comments.setContent(content);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        comments.setCreatedDate(formattedDate);
        comments.setPatient(patient);
        comments.setDoctor(doctor);
        rep.save(comments);
        return "Uploaded Successfully";
    }
    
    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Report> entityType =  entityManager.getMetamodel().entity(Report.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Report, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
}
