package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Pharmacy",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username","typ"}),
    @UniqueConstraint(columnNames = {"email","typ"}),
    @UniqueConstraint(columnNames = {"pno","typ"})
})
public class Pharmacy extends Userr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PharmacyID;

    @Column
    private String openingHours;



}
