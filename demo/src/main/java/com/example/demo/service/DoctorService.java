package com.example.demo.service;
import com.example.demo.model.Doctor;
import com.example.demo.repo.DoctorRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;

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

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo rep;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir2}")
    private String imageuploaddir;

    public String Saveprofilepicture(MultipartFile file, Doctor doctor){
        String currentReportpath = imageuploaddir + doctor.getProfilepicture();
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
                doctor.setProfilepicture(filename);    
                file.transferTo(destinationFile);
                rep.save(doctor);
                return "/images/" + filename;
            }catch(IOException e){
                e.printStackTrace();
                return "/images/defaultpp.jpg";
            }
    }
    public Boolean updatedoctor(Map<String,String>formData,Doctor doctor){
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
                    Method setter = Doctor.class.getMethod(methodName, String.class);
                    setter.invoke(doctor,res);
            }
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return false;
        }
        rep.save(doctor);
        return true;
    }
    public boolean validate(String username, String password){
        Doctor doctor = rep.findByUsername(username);
        if (doctor != null && passwordEncoder.matches(password,doctor.getPassword())){
            return true;
        }
        return false;
    }

    public String addDoctor(Doctor doc){
        if (rep.findByUsernameAndTyp(doc.getUsername(),doc.getTyp()) != null){
            return "Username already taken";
        }
        if (rep.findByEmailAndTyp(doc.getEmail(),doc.getTyp()) != null){
            return "email already taken";
        }
        if (rep.findByPnoAndTyp(doc.getPno(),doc.getTyp()) != null){
            return "Pno already taken";
        }
        rep.save(doc);
        return "success";
    }
    public Doctor getDoctorByID(Long DocID){
        return rep.findById(DocID).orElse(new Doctor());
    } 
    public Doctor getDoctorByUsername(String username){
        return rep.findByUsername(username);
    }
    public void deleteDoctor(Long DocID){
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
