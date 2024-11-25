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
@Table(name = "PatientComments")
public class PatientComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PatientCommentID;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String createdDate;

    @ManyToOne
    @JoinColumn(name = "PID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "DID", nullable = false)
    private Doctor doctor;
}
