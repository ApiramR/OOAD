package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Report;
import com.example.demo.repo.ReportRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class ReportService {
    @Autowired
    ReportRepo rep;
    
    public String addReport(Report report){
        if (rep.findByTitle(report.getTitle()) != null){
            return "Title Already exist";
        }
        rep.save(report);
        return "Uploaded Successfully";
    }
    
    public void updateReport(Report report){
        rep.save(report);
    }

    public Report getReportByID(Long reportID){
        return rep.findById(reportID).orElse(new Report());
    } 
    public void deleteReport(Long reportID){
        rep.deleteById(reportID);
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
