package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    public boolean validate(String username, String password){
        Patient patient = rep.findByUsername(username);
        if (patient != null && passwordEncoder.matches(password,patient.getPassword())){
            return true;
        }
        return false;
    }
    public String Saveprofilepicture(MultipartFile file, Patient patient){
        String currentReportpath = imageuploaddir + patient.getProfilepicture();
            File currentpicture = new File(currentReportpath);
            if (currentpicture.exists()){
                System.out.println("Why its not deletingggg");
                currentpicture.delete();
            }
            long l = System.currentTimeMillis();
            String s = l + "";
            String filename = 'a' + s + '_' + file.getOriginalFilename();
            System.out.println(file.getOriginalFilename());
            File directory = new File(imageuploaddir);
            if (!directory.exists()){
                directory.mkdirs();
            }
            try{
                File destinationFile = new File(directory,filename);
                patient.setProfilepicture(filename);    
                file.transferTo(destinationFile);
                rep.save(patient);
                return "/images/" + filename;
                
            }catch(IOException e){
                e.printStackTrace();
                return "/images/defaultpp.jpg";
            }
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
    
    public Boolean updatePatient(Map<String,String>formData,Patient patient){
        try{
            for (Map.Entry<String, String> column : formData.entrySet()) {
                String fieldName = column.getKey();
                Object fieldValue = column.getValue();
                if (fieldName.equals("profilepicture")){
                    continue;
                }
                if (fieldValue == null)continue;
                if (fieldValue.equals(""))continue;
                System.out.println(fieldName + " " + (String)fieldValue);
                    String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                    String res = (String)fieldValue;
                    if (fieldName.equals("Password")){
                        System.out.println(res);
                        res = passwordEncoder.encode(res);
                        System.out.println(res);
                    }
                    else if (fieldName.equals("height") || fieldName.equals("weight")){
                        Method setter = Patient.class.getMethod(methodName, Double.class);
                        setter.invoke(patient,Double.parseDouble(res));
                        continue;    
                    }
                    Method setter = Patient.class.getMethod(methodName, String.class);
                    setter.invoke(patient,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return false;
        }
        rep.save(patient);
        return true;
    }

    public Patient getPatientByID(Long patientID){
        return rep.findById(patientID).orElse(null);
    } 
    public Patient getPatientByUsername(String username){
        return rep.findByUsername(username);
    }
    public void deletePatient(Long patientID){
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
