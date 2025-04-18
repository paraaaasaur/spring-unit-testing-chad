package com.herbivore.springmvc.model;

import lombok.Getter;
import lombok.Setter;

public class GradebookCollegeStudent extends CollegeStudent {
    @Getter @Setter
    private StudentGrades studentGrades;


    public GradebookCollegeStudent(String firstname, String lastname, String emailAddress) {
        super(firstname, lastname, emailAddress);
    }

    public GradebookCollegeStudent(int id, String firstname, String lastname, String emailAddress, StudentGrades studentGrades) {
        super(firstname, lastname, emailAddress);
        this.studentGrades = studentGrades;
        super.setId(id);
    }
}
