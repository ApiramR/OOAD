package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryID;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private int quantityInStock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medID", nullable = false)
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

}

