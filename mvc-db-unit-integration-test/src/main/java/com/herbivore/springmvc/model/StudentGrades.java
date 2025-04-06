package com.herbivore.springmvc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Getter @Setter @ToString
public class StudentGrades {
    private List<Grade> mathGradeResults;

    private List<Grade> scienceGradeResults;

    private List<Grade> historyGradeResults;

    public StudentGrades() {}

    public double addGradeResultsForSingleClass(List<Grade> grades) {
        double result = 0;

        for (Grade grade : grades) {
            result += grade.getGrade();
        }

        return result;
    }

    public double findGradePointAverage (List<Grade> grades) {
        int lengthOfGrades = grades.size();
        double sum = addGradeResultsForSingleClass(grades);
        double result = sum / lengthOfGrades;

        // add a round function
        BigDecimal resultRound = BigDecimal.valueOf(result);
        resultRound = resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();
    }
}
