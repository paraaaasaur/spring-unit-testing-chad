package com.herbivore.springmvc.service;

import com.herbivore.springmvc.model.*;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.herbivore.springmvc.model.Grade.Type.*;

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
		if (isStudentFound(id)) {
			studentDao.deleteById(id);
		}
	}

	public Iterable<CollegeStudent> getGradebook() {
		return studentDao.findAll();
	}

	public boolean createGrade(double grade, int studentId, Grade.Type gradeType) {
		// 1. validate conditions
		CollegeStudent student = studentDao.findById(studentId).orElse(null);
		boolean valid = (grade >= 0.0 && grade <= 100.0) && (student != null);
		if (!valid) return false;

		// 2. create grade entity & save to DB
		switch (gradeType) {
			case HISTORY -> {
				HistoryGrade historyGrade = new HistoryGrade();
	//				historyGrade.setId(0);
					historyGrade.setGrade(grade);
					student.associate(historyGrade);
				historyGradeDao.save(historyGrade);
			}
			case MATH -> {
				MathGrade mathGrade = new MathGrade();
	//				mathGrade.setId(0);
					mathGrade.setGrade(grade);
					student.associate(mathGrade);
				mathGradeDao.save(mathGrade);
			}
			case SCIENCE -> {
				ScienceGrade scienceGrade = new ScienceGrade();
	//				scienceGrade.setId(0);
					scienceGrade.setGrade(grade);
					student.associate(scienceGrade);
				scienceGradeDao.save(scienceGrade);
			}
			default -> {
				return false;
			}
		}


		return true;
	}

	public int deleteGrade(int gradeId, Grade.Type gradeType) {
		int studentId = 0;

		if (gradeType == MATH) {
			Optional<MathGrade> mathGrade = mathGradeDao.findById(gradeId);
			if (mathGrade.isPresent()) {
				studentId = mathGrade.get().getCollegeStudent().getId();
				mathGradeDao.delete(mathGrade.get());
			}
		}

		if (gradeType == SCIENCE) {
			Optional<ScienceGrade> scienceGrade = scienceGradeDao.findById(gradeId);
			if (scienceGrade.isPresent()) {
				studentId = scienceGrade.get().getCollegeStudent().getId();
				scienceGradeDao.delete(scienceGrade.get());
			}
		}

		if (gradeType == HISTORY) {
			Optional<HistoryGrade> historyGrade = historyGradeDao.findById(gradeId);
			if (historyGrade.isPresent()) {
				studentId = historyGrade.get().getCollegeStudent().getId();
				historyGradeDao.delete(historyGrade.get());
			}
		}

		return studentId;
	}

	public GradesAndCollegeStudent studentInformation(int studentId) {
		CollegeStudent student = studentDao.findById(studentId).orElse(null);

		if (student == null) {
			return null;
		}

		var historyGradeList = iterToList(historyGradeDao.findGradesByCollegeStudentId(studentId));
		var mathGradeList = iterToList(mathGradeDao.findGradesByCollegeStudentId(studentId));
		var scienceGradeList = iterToList(scienceGradeDao.findGradesByCollegeStudentId(studentId));

		StudentGrades studentGrades = new StudentGrades();
		studentGrades.setHistoryGradeResults(historyGradeList);
		studentGrades.setMathGradeResults(mathGradeList);
		studentGrades.setScienceGradeResults(scienceGradeList);


		return new GradesAndCollegeStudent(studentGrades, student);
	}


	// helper methods
	private <T extends Grade> List<T> iterToList(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false)
				.toList();
	}
}
