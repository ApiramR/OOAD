package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    private Long PharmacyID;

    @Column
    private String openingHours;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories; // One Pharmacy with Many Inventories


}
