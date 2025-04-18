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

		if (grade < 100.0 && grade > 0.0) {
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
		}

		return false;
	}

	public int deleteGrade(int gradeId, Class<? extends Grade> gradeClazz) {
		int studentId = 0;

		if (gradeClazz == MathGrade.class) {
			Optional<MathGrade> mathGrade = mathGradeDao.findById(gradeId);
			if (mathGrade.isPresent()) {
				studentId = mathGrade.get().getStudentId();
				mathGradeDao.delete(mathGrade.get());
			}
		}

		if (gradeClazz == ScienceGrade.class) {
			Optional<ScienceGrade> scienceGrade = scienceGradeDao.findById(gradeId);
			if (scienceGrade.isPresent()) {
				studentId = scienceGrade.get().getStudentId();
				scienceGradeDao.delete(scienceGrade.get());
			}
		}

		if (gradeClazz == HistoryGrade.class) {
			Optional<HistoryGrade> historyGrade = historyGradeDao.findById(gradeId);
			if (historyGrade.isPresent()) {
				studentId = historyGrade.get().getStudentId();
				historyGradeDao.delete(historyGrade.get());
			}
		}

		return studentId;
	}
}
