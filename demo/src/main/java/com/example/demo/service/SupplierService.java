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
import com.example.demo.model.Supplier;
import com.example.demo.repo.SupplierRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

@Service
public class SupplierService {
    @Autowired
    SupplierRepo rep;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    public boolean validate(String username, String password){
        Supplier supplier = rep.findByUsername(username);
        if (supplier != null && passwordEncoder.matches(password,supplier.getPassword())){
            return true;
        }
        return false;
    }

    public String addSupplier(Supplier supplier){
        if (rep.findByUsernameAndTyp(supplier.getUsername(),supplier.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(supplier.getEmail(),supplier.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(supplier.getPno(),supplier.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(supplier);
        return "success";
    }
    
    public void updateSupplier(Supplier supplier){
        rep.save(supplier);
    }

    public Supplier getSupplierByID(int supplierID){
        return rep.findById(supplierID).orElse(new Supplier());
    } 
    public Supplier getSupplierByUsername(String username){
        return rep.findByUsername(username);
    }
    public String Saveprofilepicture(MultipartFile file, Supplier supplier){
        String currentReportpath = imageuploaddir + supplier.getProfilepicture();
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
                supplier.setProfilepicture(filename);    
                file.transferTo(destinationFile);
                rep.save(supplier);
                return "/images/" + filename;
                
            }catch(IOException e){
                e.printStackTrace();
                return "/images/defaultpp.jpg";
            }
    }
    public Boolean updatePatient(Map<String,String>formData,Supplier supplier){
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
                        setter.invoke(supplier,Double.parseDouble(res));
                        continue;    
                    }
                    Method setter = Patient.class.getMethod(methodName, String.class);
                    setter.invoke(supplier,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return false;
        }
        rep.save(supplier);
        return true;
    }
    public void deleteSupplier(int supplierID){
        rep.deleteById(supplierID);
    }
    @Autowired
    private EntityManager entityManager;

    public String[] getFields(){
        EntityType<Supplier> entityType =  entityManager.getMetamodel().entity(Supplier.class);
        List<String> columnNames = new ArrayList<>();
        for (Attribute<? super Supplier, ?> attribute : entityType.getAttributes()) {
            columnNames.add(attribute.getName());
        }
        return columnNames.toArray(new String[0]);
    }
}
