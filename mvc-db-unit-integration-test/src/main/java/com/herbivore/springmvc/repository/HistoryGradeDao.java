package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.HistoryGrade;

public interface HistoryGradeDao extends GradeDao<HistoryGrade> {
	Iterable<HistoryGrade> findAllByCollegeStudentId(int studentId);

	void deleteByCollegeStudentId(int studentId);
}