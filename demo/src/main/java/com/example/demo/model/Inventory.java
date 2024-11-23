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
@Entity
@Table(name = "Inventory")
public class Inventory extends Medicine{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long inventoryID;

    private long price;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @Column(nullable = false)
    private int quantityInStock;

}
