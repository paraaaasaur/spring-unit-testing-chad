package com.herbivore.springmvc.service;

import com.herbivore.springmvc.model.*;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {
	private final StudentDao studentDao;
	private final MathGradeDao mathGradeDao;
	private final ScienceGradeDao scienceGradeDao;
	private final HistoryGradeDao historyGradeDao;


	public StudentAndGradeService(StudentDao studentDao, MathGradeDao mathGradeDao, ScienceGradeDao scienceGradeDao, HistoryGradeDao historyGradeDao) {
		this.studentDao = studentDao;
		this.mathGradeDao = mathGradeDao;
		this.scienceGradeDao = scienceGradeDao;
		this.historyGradeDao = historyGradeDao;
	}


	public void createStudent(String firstname, String lastname, String emailAddress) {
		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
		student.setId(0);
		studentDao.save(student);
	}

	public boolean isStudentFound(int id) {
		return studentDao.findById(id).isPresent();
	}

	public void deleteStudent(int id) {
		if (isStudentFound(id))
			studentDao.deleteById(id);
	}

	public Iterable<CollegeStudent> getGradebook() {
		return studentDao.findAll();
	}

	public boolean createGrade(double grade, int studentId, Grade.Type gradeType) {
		// 1. validate conditions
		boolean valid = (grade >= 0.0 && grade <= 100.0) && isStudentFound(studentId);
		if (!valid) return false;

		// 2. create grade entity & save to DB
		switch (gradeType) {
			case HISTORY -> {
				HistoryGrade historyGrade = new HistoryGrade();
				//				historyGrade.setId(0);
				historyGrade.setGrade(grade);
				historyGrade.setStudentId(studentId);
				historyGradeDao.save(historyGrade);
			}
			case MATH -> {
				MathGrade mathGrade = new MathGrade();
				//				mathGrade.setId(0);
				mathGrade.setGrade(grade);
				mathGrade.setStudentId(studentId);
				mathGradeDao.save(mathGrade);
			}
			case SCIENCE -> {
				ScienceGrade scienceGrade = new ScienceGrade();
				//				scienceGrade.setId(0);
				scienceGrade.setGrade(grade);
				scienceGrade.setStudentId(studentId);
				scienceGradeDao.save(scienceGrade);
			}
			default -> {
				return false;
			}
		}


		return true;
	}
}
