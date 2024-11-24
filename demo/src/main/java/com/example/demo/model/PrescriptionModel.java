package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="prescriptions")
public class PrescriptionModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="presid")
    private Long presID;

    @Column(name="doctorID",nullable=false)
    private String doctorId;

    @Column(name="patientID",nullable=false)
    private String patientId;

    @Column(name="meds",nullable=false)
    private String meds;

    @Column(name="dosage",nullable=false)
    private String dosage;

    @Column(name="dateIssued",nullable=false)
    private String dateIssued;

    @Column(name="description",nullable=true)
    private String description;


}
