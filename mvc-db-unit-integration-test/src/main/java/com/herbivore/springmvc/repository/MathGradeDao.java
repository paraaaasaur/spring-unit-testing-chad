package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.MathGrade;

public interface MathGradeDao extends GradeDao<MathGrade> {
	Iterable<MathGrade> findAllByCollegeStudentId(int studentId);

	void deleteByCollegeStudentId(int studentId);
}