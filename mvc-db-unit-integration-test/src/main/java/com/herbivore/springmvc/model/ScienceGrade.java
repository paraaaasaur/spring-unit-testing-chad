package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "science_grade")
@Getter @Setter @ToString
public class ScienceGrade implements Grade {
    @Id @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "grade")
    private double grade;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    @Setter(PROTECTED) @ToString.Exclude
    private CollegeStudent collegeStudent;


    public ScienceGrade() {}

    public ScienceGrade(double grade) {
        this.grade = grade;
    }

    public ScienceGrade(int studentId, double grade) {
        this.studentId = studentId;
        this.grade = grade;
    }


    // #equals and #hashCode
    @Override
    public final boolean equals(Object o) {
        return (o instanceof ScienceGrade that)
               && this.getId() == that.getId()
               && this.getStudentId() == that.getStudentId();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId(), getStudentId());
    }
}