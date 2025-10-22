package com.herbivore.springmvc.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.herbivore.springmvc.exception.ApiException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @Setter(PROTECTED)
    @BatchSize(size = 100)
    @JsonManagedReference
    private Set<HistoryGrade> historyGrades = new HashSet<>();

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @Setter(PROTECTED)
    @BatchSize(size = 100)
    @JsonManagedReference
    private Set<MathGrade> mathGrades = new HashSet<>();

    @OneToMany(mappedBy = "collegeStudent", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @Setter(PROTECTED)
    @BatchSize(size = 100)
    @JsonManagedReference
    private Set<ScienceGrade> scienceGrades = new HashSet<>();


    public CollegeStudent() {}

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }


    // JPA convenience methods
    public void associate(Grade grade) {
        grade.setCollegeStudent(this);
        resolveCollection(grade).add(grade);
    }

    public void dissociate(Grade grade) {
        grade.setCollegeStudent(null);
        resolveCollection(grade).remove(grade);
    }


    // custom methods
    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    public String getFullNameAndEmailAddress() {
        return getFullName() + " " + getEmailAddress();
    }


    // helper methods
    @SuppressWarnings("unchecked")
    private <T extends Grade> Set<T> resolveCollection(T grade) {
        Grade.Subject subject = Grade.Subject.of(grade.getClass());
        return (Set<T>) switch (subject) {
			case HISTORY -> this.historyGrades; // Set<HistoryGrade>
			case MATH -> this.mathGrades;
			case SCIENCE -> this.scienceGrades;
			default -> throw new ApiException("Invalid subject: " + subject);
		};
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