package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass

public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medID;


    @Column(nullable = false)
    private String medName;
    @Column(nullable = false)
    private String medType;
    @Column(nullable = false)
    private String strength;
    @Column
    private String manufacturer;

}
