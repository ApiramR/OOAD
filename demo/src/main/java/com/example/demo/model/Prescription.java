package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="Prescription")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="presid")
    private Long presID;

    /*@ManyToOne
    @JoinColumn(name = "dID", nullable = false)
    private Doctor doctor;
    */

    @ManyToOne
    @JoinColumn(name="PID",nullable=false)
    private Patient patient;

    @Column(nullable=false)
    private String meds;

    @Column(nullable=false)
    private String dosage;

    @Column(nullable=false)
    private String dateIssued;

    @Column(nullable=true)
    private String description;

}
