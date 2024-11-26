package com.example.demo.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "MID", nullable = false)
    private Medicine meds;

    @Column(nullable=false)
    private String dosage;

    @Column(nullable=false)
    private String dateIssued;

    @Column(nullable=true)
    private String description;

    @Column(nullable=false)
    private int count;

    @ManyToOne
    @JoinColumn(name = "PID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "DID", nullable = false)
    private Doctor doctor;

    

    public Prescription(int count,Medicine meds,String dosage,String dataIssued,String description,Patient patient,Doctor doctor){
        this.meds = meds;
        this.dosage = dosage;
        this.dateIssued = dataIssued;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.count = count;
    }

}



