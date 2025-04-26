package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradeDao extends CrudRepository<ScienceGrade, Integer> {
	Iterable<ScienceGrade> findGradesByCollegeStudentId(int studentId);

	void deleteByCollegeStudentId(int studentId);
}
