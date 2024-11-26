package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Patient;
import com.example.demo.model.Report;
import com.example.demo.repo.ReportRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class ReportService {
    @Autowired
    ReportRepo rep;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String addReport(MultipartFile file,String title,Patient patient){
        if (rep.findByTitle(title) != null){
            return "Title Already exist";
        }
        try{
            long l = System.currentTimeMillis();
            String s = l + "";
            String filename = 'a' + s + '_' + file.getOriginalFilename();
            File directory = new File(uploadDir);
            if (!directory.exists()){
                directory.mkdirs();
            }
            File destinationFile = new File(directory,filename);
            Report report = new Report();
            report.setTitle(title);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            report.setCreatedDate(formattedDate);
            report.setUpdatedDate(formattedDate);
            report.setReportfile(filename);
            report.setPatient(patient);
            file.transferTo(destinationFile);
            rep.save(report);
            return "Uploaded Successfully";
        }
        catch(IOException e){
            e.printStackTrace();
            return "Error Occured Please Try Again!";
        }
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
