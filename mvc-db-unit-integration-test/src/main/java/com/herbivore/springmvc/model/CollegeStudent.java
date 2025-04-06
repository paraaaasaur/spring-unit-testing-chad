package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@Getter @Setter @ToString
public class CollegeStudent implements Student {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(name="email_address")
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

    public String studentInformation() {
       return getFullName() + " " + getEmailAddress();
    }
}
