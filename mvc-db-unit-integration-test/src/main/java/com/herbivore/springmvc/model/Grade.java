package com.herbivore.springmvc.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public interface Grade {
    double getGrade();

    int getId();

    void setId(int id);

    int getStudentId();

    void setStudentId(int studentId);

    void setGrade(double grade);


    // utility methods
    static double sumGrades(List<? extends Grade> grades) {
		return grades.stream()
				.mapToDouble(Grade::getGrade)
				.sum();
    }

    static double avgGrades (List<? extends Grade> grades) {
        double result = sumGrades(grades) / grades.size();

        // add a round function
        BigDecimal resultRound = BigDecimal.valueOf(result);
        resultRound = resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();
    }


    // enum for grade types
    @Getter
    enum Type {
        HISTORY(HistoryGrade.class),
        MATH(MathGrade.class),
        SCIENCE(ScienceGrade.class),
        UNKNOWN(Grade.class);

        private final Class<? extends Grade> implClass;

        Type(Class<? extends Grade> implClass) {
            this.implClass = implClass;
        }
    }
}
