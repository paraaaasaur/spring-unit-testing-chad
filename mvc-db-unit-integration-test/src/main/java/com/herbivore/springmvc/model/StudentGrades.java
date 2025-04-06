package com.herbivore.springmvc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public final class StudentGrades {
    private List<MathGrade> mathGradeResults;

    private List<ScienceGrade> scienceGradeResults;

    private List<HistoryGrade> historyGradeResults;


    public StudentGrades() {
        this.mathGradeResults = new ArrayList<>();
        this.scienceGradeResults = new ArrayList<>();
        this.historyGradeResults = new ArrayList<>();
    }

    public StudentGrades(List<MathGrade> mathGradeResults, List<ScienceGrade> scienceGradeResults, List<HistoryGrade> historyGradeResults) {
        this.mathGradeResults = mathGradeResults;
        this.scienceGradeResults = scienceGradeResults;
        this.historyGradeResults = historyGradeResults;
    }
}
