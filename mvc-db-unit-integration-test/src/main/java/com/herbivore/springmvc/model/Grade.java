package com.herbivore.springmvc.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Grade {
    double getGrade();

    int getId();

    void setId(int id);

    void setGrade(double grade);

    CollegeStudent getCollegeStudent();

    void setCollegeStudent(CollegeStudent collegeStudent);

    // utility methods
    static double sumGrades(List<? extends Grade> grades) {
		return grades.stream()
				.mapToDouble(Grade::getGrade)
				.sum();
    }

    static double avgGrades(List<? extends Grade> grades) {
        double result = sumGrades(grades) / grades.size();

        // add a round function
        BigDecimal resultRound = BigDecimal.valueOf(result);
        resultRound = resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();
    }


    // enum for grade subjects
    @Getter
    enum Subject {
        HISTORY(HistoryGrade.class),
        MATH(MathGrade.class),
        SCIENCE(ScienceGrade.class),
        UNKNOWN(Grade.class);


        private final Class<? extends Grade> entityClass;

        private static final Map<Class<? extends Grade>, Subject> ENTITYCLASS_ENUM_MAP =
                Arrays.stream(values())
                    .collect(Collectors.toMap(
                            Subject::getEntityClass,
                            subject -> subject
                    ));


        Subject(Class<? extends Grade> entityClass) {
            this.entityClass = entityClass;
        }


        public static Subject of(@NotNull Class<? extends Grade> entityClazz) {
            // Option 1. Straightforward logic
            if (true) {
                Subject subject = ENTITYCLASS_ENUM_MAP.get(entityClazz);
                if (subject == null) {
                    throw new IllegalArgumentException("Unknown grade: " + entityClazz);
                }
                return subject;
            }

            // Option 2. Functional style using Optional<T>
            if (false){
                return Optional
                        .ofNullable(ENTITYCLASS_ENUM_MAP.get(entityClazz))
                        .orElseThrow(() -> new IllegalArgumentException("Unknown grade: " + entityClazz));
            }

            return UNKNOWN;
        }
    }
}