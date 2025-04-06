package com.herbivore.springmvc.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter @Setter
public class Gradebook {
    private List<GradebookCollegeStudent> students = new ArrayList<>();


    public Gradebook() {}

    public Gradebook(List<GradebookCollegeStudent> students) {
        this.students = students;
    }
}
