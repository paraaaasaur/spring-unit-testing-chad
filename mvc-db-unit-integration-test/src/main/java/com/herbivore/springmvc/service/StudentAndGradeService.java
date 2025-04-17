package com.herbivore.springmvc.service;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.HistoryGrade;
import com.herbivore.springmvc.model.MathGrade;
import com.herbivore.springmvc.model.ScienceGrade;
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
	private final MathGrade mathGrade;
	private final MathGradeDao mathGradeDao;
	private final ScienceGrade scienceGrade;
	private final ScienceGradeDao scienceGradeDao;
	private final HistoryGrade historyGrade;
	private final HistoryGradeDao historyGradeDao;


	public StudentAndGradeService(StudentDao studentDao, MathGrade mathGrade, MathGradeDao mathGradeDao, ScienceGrade scienceGrade, ScienceGradeDao scienceGradeDao, HistoryGrade historyGrade, HistoryGradeDao historyGradeDao) {
		this.studentDao = studentDao;
		this.mathGrade = mathGrade;
		this.mathGradeDao = mathGradeDao;
		this.scienceGrade = scienceGrade;
		this.scienceGradeDao = scienceGradeDao;
		this.historyGrade = historyGrade;
		this.historyGradeDao = historyGradeDao;
	}


	public void createStudent(String firstname, String lastname, String emailAddress) {
		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
		student.setId(0);
		studentDao.save(student);
	}

	public boolean checkIfStudentIsNull(int id) {
		Optional<CollegeStudent> studentOp = studentDao.findById(id);
		return studentOp.isPresent();
	}

	public void deleteStudent(int id) {
		if (checkIfStudentIsNull(id))
			studentDao.deleteById(id);
	}

	public Iterable<CollegeStudent> getGradebook() {
		return studentDao.findAll();
	}

	public boolean createGrade(double grade, int studentId, String subject) {
		if (!checkIfStudentIsNull(studentId)) {
			return false;
		}

		if (grade < 0.0 || grade > 100.0) {
			return false;
		}

		if ("math".equalsIgnoreCase(subject)) {
			mathGrade.setGrade(grade);
			mathGrade.setStudentId(studentId);
			mathGradeDao.save(mathGrade);
			return true;
		}

		if ("science".equalsIgnoreCase(subject)) {
			scienceGrade.setGrade(grade);
			scienceGrade.setStudentId(studentId);
			scienceGradeDao.save(scienceGrade);
			return true;
		}

		if ("history".equalsIgnoreCase(subject)) {
			historyGrade.setGrade(grade);
			historyGrade.setStudentId(studentId);
			historyGradeDao.save(historyGrade);
			return true;
		}

		return false;
	}
}
