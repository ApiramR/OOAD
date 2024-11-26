package com.example.demo.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Patient",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username","typ"}),
    @UniqueConstraint(columnNames = {"email","typ"}),
    @UniqueConstraint(columnNames = {"pno","typ"})
})
public class Patient extends Userr{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PatientID;

    @Column()
    private String bloodtype;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;
    
    public BMI getBMI(){
        return new BMI(height,weight);
    }
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientComments> comments = new ArrayList<>();    
    
    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (this.bloodtype== null) {
            this.bloodtype = "unknown";  // Default path
        }
    }
}

@Getter
@Setter
class BMI {
    private Double height;
    private Double weight;
    private Double value;
    public BMI(Double height,Double weight){
        this.height = height;
        this.weight = weight;
        value = 0.0;
        if (height > 0){
            value = weight / (height * height);
        }
    }
}