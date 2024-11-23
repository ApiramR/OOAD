package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "Pharmacy_Inventory",
            joinColumns = @JoinColumn(name = "pharmacy_id"),
            inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private List<Inventory> inventories; // Many-to-Many relationship

}
