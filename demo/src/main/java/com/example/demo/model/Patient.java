package com.example.demo.model;

import com.example.demo.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Patient")
public class Patient extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PatientID;
}
