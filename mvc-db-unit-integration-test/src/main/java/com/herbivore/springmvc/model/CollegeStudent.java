package com.herbivore.springmvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL)
    @ToString.Exclude @Setter(PROTECTED)
    private List<HistoryGrade> historyGrades;

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL)
    @ToString.Exclude @Setter(PROTECTED)
    private List<MathGrade> mathGrades;

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL)
    @ToString.Exclude @Setter(PROTECTED)
    private List<ScienceGrade> scienceGrades;


    public CollegeStudent() {}

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }


    // JPA convenience methods
    public void associate(MathGrade mathGrade) {
        mathGrade.setCollegeStudent(this);
        this.mathGrades.add(mathGrade);
    }

    public void associate(HistoryGrade historyGrade) {
        historyGrade.setCollegeStudent(this);
        this.historyGrades.add(historyGrade);
    }

    public void associate(ScienceGrade scienceGrade) {
        scienceGrade.setCollegeStudent(this);
        this.scienceGrades.add(scienceGrade);
    }

    public void dissociate(MathGrade mathGrade) {
        mathGrade.setCollegeStudent(null);
        mathGrades.remove(mathGrade);
    }

    public void dissociate(HistoryGrade historyGrade) {
        historyGrade.setCollegeStudent(null);
        historyGrades.remove(historyGrade);
    }

    public void dissociate(ScienceGrade scienceGradeGrade) {
        scienceGradeGrade.setCollegeStudent(null);
        scienceGrades.remove(scienceGradeGrade);
    }


    // custom methods
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