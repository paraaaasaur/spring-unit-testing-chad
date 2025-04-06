package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "student")
@Getter @Setter @ToString
public class CollegeStudent {
    @Id @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(name = "email_address")
    private String emailAddress;


    public CollegeStudent() {}

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }


    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    public String getFullNameAndEmailAddress() {
        return getFullName() + " " + getEmailAddress();
    }


    // #equals and #hashCode
    @Override
    public final boolean equals(Object o) {
        return (o instanceof CollegeStudent that)
               && this.getId() == that.getId()
               && this.getFullName().equals(that.getFullName());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId(), getFullName());
    }
}