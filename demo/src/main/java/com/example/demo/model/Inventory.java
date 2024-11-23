package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Inventory")
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

    @ManyToMany(mappedBy = "inventories")
    private List<Pharmacy> pharmacies; // Reverse side of Many-to-Many



    public Inventory(long inventoryID, long medID, long price, LocalDate expiryDate, int quantityInStock) {
        this.expiryDate = expiryDate;
        this.inventoryID = inventoryID;
        this.medicine = medicine;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.pharmacies = pharmacies;
    }


    //getter and setters

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Long inventoryID) {
        this.inventoryID = inventoryID;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine() {
        this.medicine = medicine;
    }

    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
