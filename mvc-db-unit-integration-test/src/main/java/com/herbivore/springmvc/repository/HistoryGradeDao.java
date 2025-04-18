package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDao extends CrudRepository<HistoryGrade, Integer> {
	Iterable<HistoryGrade> findGradeByStudentId(int studentId);

	void deleteByStudentId(int studentId);
}
