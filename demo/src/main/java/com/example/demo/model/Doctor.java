package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Doctor",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username","typ"}),
    @UniqueConstraint(columnNames = {"email","typ"}),
    @UniqueConstraint(columnNames = {"pno","typ"})
})
public class Doctor extends Userr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DocID;

    @Column(nullable = false)
    private String Specialization;
    
}
