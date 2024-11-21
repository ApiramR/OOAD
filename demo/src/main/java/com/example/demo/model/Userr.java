package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@MappedSuperclass
public class Userr {

    @Column(nullable = false)
    private String Fname;
    
    private String Mname;

    @Column(nullable = false)
    private String Lname;
    
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false , unique = true)
    private String Pno;

    @Column(nullable = false)
    private LocalDate DOB;

    @Column(nullable = false)
    private String Gender;

    @Column(nullable = false)
    private String Address;

    @Column(nullable = false)
    private String typ;

    @Column(nullable = false)
    private String Password;

    
    /*@ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @joinTable(
        name = "user_roles",
        joinColumns = {@joinColumn(name="User_ID" , referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "Role_ID" , referencedColumnName = "ID")}
    )*/

}