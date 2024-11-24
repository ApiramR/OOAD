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

    @Column(nullable = false, unique = true)
    private String title;

    @Column()
    private String createdDate;

    @Column()
    private String updatedDate;

    @Column()
    private Long DocID;

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
