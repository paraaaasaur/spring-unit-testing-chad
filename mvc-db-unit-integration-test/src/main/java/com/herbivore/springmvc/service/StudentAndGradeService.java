package com.herbivore.springmvc.service;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.MathGrade;
import com.herbivore.springmvc.repository.MathGradeDao;
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


	public StudentAndGradeService(StudentDao studentDao, MathGrade mathGrade, MathGradeDao mathGradeDao) {
		this.studentDao = studentDao;
		this.mathGrade = mathGrade;
		this.mathGradeDao = mathGradeDao;
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

		return false;
	}
}
