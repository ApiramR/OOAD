package com.example.demo.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Report;
import com.example.demo.repo.ReportRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class ReportService {
    @Autowired
    ReportRepo rep;

    /*@Value("${file.upload-dir}")
    private String uploadDir;

    /*public Report addReport(MultipartFile file,String title) throws Exception{
        File directory = new File(uploadDir);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(directory, filename);
        file.transferTo(destinationFile);
        Report report = new Report();
        report.setTitle(title);
        report.setCreatedDate(LocalDateTime.now());
        rep.save(report);
        return report;
    }*/
    
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
