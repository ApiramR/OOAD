package com.example.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
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
    
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pno;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate DOB;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String typ;

    @Column(nullable = false)
    private String Password;

    @Column(nullable = false)
    private String profilepicture;

    @PrePersist
    public void prePersist() {
        if (this.profilepicture == null) {
            this.profilepicture = "defaultpp.jpg";  // Default path
        }
    }
    
    /*@ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @joinTable(
        name = "user_roles",
        joinColumns = {@joinColumn(name="User_ID" , referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "Role_ID" , referencedColumnName = "ID")}
    )*/

}
