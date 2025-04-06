package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "math_grade")
@Getter @Setter
public class MathGrade implements Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="student_id")
    private int studentId;

    @Column(name="grade")
    private double grade;


    public MathGrade() {}

    public MathGrade(double grade) {
        this.grade = grade;
    }
}
