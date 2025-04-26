package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDao extends CrudRepository<MathGrade, Integer> {
	Iterable<MathGrade> findGradesByCollegeStudentId(int studentId);

	void deleteByCollegeStudentId(int studentId);
}
