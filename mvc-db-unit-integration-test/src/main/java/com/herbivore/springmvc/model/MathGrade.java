package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Table(name = "math_grade")
@Getter @Setter @ToString
public class MathGrade implements Grade {
    @Id @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "grade")
    private double grade;


    public MathGrade() {}

    public MathGrade(double grade) {
        this.grade = grade;
    }

    public MathGrade(int studentId, double grade) {
        this.studentId = studentId;
        this.grade = grade;
    }


    // #equals and #hashCode
    @Override
    public final boolean equals(Object o) {
        return (o instanceof MathGrade that)
               && this.getId() == that.getId()
               && this.getStudentId() == that.getStudentId();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId(), getStudentId());
    }
}