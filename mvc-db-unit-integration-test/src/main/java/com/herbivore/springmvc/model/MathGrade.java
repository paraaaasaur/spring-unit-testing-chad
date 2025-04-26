package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "math_grade")
@Getter @Setter @ToString
public class MathGrade implements Grade {
    @Id @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "grade")
    private double grade;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    @Setter(PROTECTED) @ToString.Exclude
    private CollegeStudent collegeStudent;


    public MathGrade() {}

    public MathGrade(double grade) {
        this.grade = grade;
    }


    // #equals and #hashCode
    @Override
    public final boolean equals(Object o) {
        return (o instanceof MathGrade that)
               && this.getId() == that.getId();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}