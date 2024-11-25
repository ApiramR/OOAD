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

import com.example.demo.model.Pharmacy;
import com.example.demo.repo.PharmacyRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PharmacyService {
    @Autowired
    PharmacyRepo rep;
    @Autowired
    private PharmacyRepo pharmacyRepo;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    public String addPharmacy(Pharmacy pharmacy){
        if (rep.findByUsernameAndTyp(pharmacy.getUsername(),pharmacy.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(pharmacy.getEmail(),pharmacy.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(pharmacy.getPno(),pharmacy.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(pharmacy);
        return "success";
    }

    public void updatePharmacy(Map<String, String> formData, Pharmacy pharmacy){
        rep.save(pharmacy);
    }

    public Boolean updatePharmacy1(Map<String,String>formData, Pharmacy pharmacy){
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
                    Method setter = Pharmacy.class.getMethod(methodName, Double.class);
                    setter.invoke(pharmacy,Double.parseDouble(res));
                    continue;
                }
                Method setter = Pharmacy.class.getMethod(methodName, String.class);
                setter.invoke(pharmacy,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Pharmacy getPharmacyByID(Long pharmacyID){
        return rep.findById(pharmacyID).orElse(new Pharmacy());
    } 
    public Pharmacy getPharmacyByUsername(String username){
        return rep.findByUsername(username);
    }
    public void deletePharmacy(Long pharmacyID){
        rep.deleteById(pharmacyID);
    }
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validate(String username, String password){
        Pharmacy pharmacy = rep.findByUsername(username);
        if (pharmacy != null && passwordEncoder.matches(password,pharmacy.getPassword())){
            return true;
        }
        return false;
    }

    public String[] getFields(){
        EntityType<Pharmacy> entityType =  entityManager.getMetamodel().entity(Pharmacy.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Pharmacy, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
    public String Saveprofilepicture(MultipartFile file, Pharmacy pharmacy){
        String currentReportpath = imageuploaddir + pharmacy.getProfilepicture();
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
            pharmacy.setProfilepicture(filename);
            file.transferTo(destinationFile);
            return "/images/" + filename;
        }catch(IOException e){
            e.printStackTrace();
            return "/images/defaultpp.jpg";
        }
    }

    public Pharmacy findById(Long id) {
        return pharmacyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
    }
}
