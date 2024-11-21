package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Doctor")
public class Doctor extends Userr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DocID;

    @Column(nullable = false)
    private String Specialization;
    
}
