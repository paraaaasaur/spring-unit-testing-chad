package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "history_grade")
@Getter @Setter
public class HistoryGrade implements Grade {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="student_id")
    private int studentId;

    @Column(name="grade")
    private double grade;


    public HistoryGrade() {}

    public HistoryGrade(double grade) {
        this.grade = grade;
    }
}
