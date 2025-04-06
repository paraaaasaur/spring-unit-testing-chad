package com.herbivore.springmvc.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
public class GradesAndCollegeStudent {
    private final StudentGrades studentGrades;

    private final CollegeStudent collegeStudent;

    public GradesAndCollegeStudent(StudentGrades studentGrades, CollegeStudent collegeStudent) {
        this.studentGrades = studentGrades;
        this.collegeStudent = collegeStudent;
    }

    public GradesAndCollegeStudent(String firstname, String lastname, String emailAddress) {
        this(new StudentGrades(), new CollegeStudent(firstname, lastname, emailAddress));
    }

    public GradesAndCollegeStudent(String firstname, String lastname, String emailAddress, StudentGrades studentGrades) {
        this(studentGrades, new CollegeStudent(firstname, lastname, emailAddress));
    }
}