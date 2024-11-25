package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Pharmacy",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username","typ"}),
    @UniqueConstraint(columnNames = {"email","typ"}),
    @UniqueConstraint(columnNames = {"pno","typ"})
})
public class Pharmacy extends Userr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pharmacyID;

    @Column
    private String openingHours;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories; // One Pharmacy with Many Inventories

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<PharmacyPrescription> pharmacyPrescriptions;
}
