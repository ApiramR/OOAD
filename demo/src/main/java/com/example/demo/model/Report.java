package com.example.demo.model;

import java.time.LocalDateTime;

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

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "Report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReportID;

    @Column()
    private String title;

    @Column()
    private LocalDateTime createdDate;

    @Column()
    private LocalDateTime updatedDate;

    @Column()
    private Long DocID;

    @Column()
    private Long PatientID;

    @Column()
    private String ReportType;

    @Column()
    private String Reportfile;

    @ManyToOne
    @JoinColumn(name = "PID", nullable = false)
    private Patient patient;


    /*@OneToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @joinTable(
        name = "patient_reports",
        joinColumns = {@joinColumn(name="Patient" , referencedColumnName = "PatientID")},
        inverseJoinColumns = {@JoinColumn(name = "Report" , referencedColumnName = "ReportID")}
    )*/

}
