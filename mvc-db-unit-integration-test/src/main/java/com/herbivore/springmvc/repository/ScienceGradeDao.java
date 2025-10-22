package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.ScienceGrade;

public interface ScienceGradeDao extends GradeDao<ScienceGrade> {
	Iterable<ScienceGrade> findAllByCollegeStudentId(int studentId);

	void deleteByCollegeStudentId(int studentId);
}