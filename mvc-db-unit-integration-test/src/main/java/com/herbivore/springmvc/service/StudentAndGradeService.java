package com.herbivore.springmvc.service;

import com.herbivore.springmvc.dto.CollegeStudentDto;
import com.herbivore.springmvc.exception.ApiException;
import com.herbivore.springmvc.exception.GradeNotFoundException;
import com.herbivore.springmvc.exception.StudentNotFoundException;
import com.herbivore.springmvc.model.*;
import com.herbivore.springmvc.repository.*;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
public class StudentAndGradeService {
	private final StudentDao studentDao;
	private final MathGradeDao mathGradeDao;
	private final ScienceGradeDao scienceGradeDao;
	private final HistoryGradeDao historyGradeDao;
	private final EntityManager em;


	public StudentAndGradeService(StudentDao studentDao, MathGradeDao mathGradeDao, ScienceGradeDao scienceGradeDao, HistoryGradeDao historyGradeDao, EntityManager em) {
		this.studentDao = studentDao;
		this.mathGradeDao = mathGradeDao;
		this.scienceGradeDao = scienceGradeDao;
		this.historyGradeDao = historyGradeDao;
		this.em = em;
	}


	public CollegeStudent createStudent(String firstname, String lastname, String emailAddress) {
		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
//		student.setId(0);
		return studentDao.save(student);
	}

	public void deleteStudent(int id) {
		CollegeStudent found = studentDao
				.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student#" + id + " not found"));
		studentDao.delete(found);
	}

	public List<CollegeStudent> findAllStudentsWithGrades() {
		List<CollegeStudent> csList;

		// option 1: default repo#findAll + manual initializing lazy fields (AKA grades)
		if (false) {
			csList = (List<CollegeStudent>) studentDao.findAll();
			for (var cs : csList) {
				Hibernate.initialize(cs.getHistoryGrades());
				Hibernate.initialize(cs.getMathGrades());
				Hibernate.initialize(cs.getScienceGrades());
			}
		}

		// option 2: precise eager fetch with JOIN FETCH jpql
		if (true) {
			//		String jpql = "SELECT cs FROM CollegeStudent cs " +
	//					  "LEFT JOIN FETCH cs.historyGrades " +
	//					  "LEFT JOIN FETCH cs.mathGrades " +
	//					  "LEFT JOIN FETCH cs.scienceGrades";

			String jpql = "SELECT cs FROM CollegeStudent cs " +
						  "LEFT JOIN FETCH cs.historyGrades";
			String jpql2 = "SELECT cs FROM CollegeStudent cs " +
						   "LEFT JOIN FETCH cs.mathGrades";
			String jpql3 = "SELECT cs FROM CollegeStudent cs " +
						   "LEFT JOIN FETCH cs.scienceGrades";

			em.createQuery(jpql2, CollegeStudent.class).getResultList();
			em.createQuery(jpql3, CollegeStudent.class).getResultList();

			csList = em.createQuery(jpql, CollegeStudent.class).getResultList();
		}

		return csList;
	}

	@SuppressWarnings("unchecked")
	public <T extends Grade> T createGrade(double grade, int studentId, Grade.Subject subject) {
		// 1. validate conditions
		CollegeStudent foundStudent = studentDao.findById(studentId).orElse(null);
		if (foundStudent == null) {
			throw new StudentNotFoundException("Student#" + studentId + " not found");
		}
		boolean isValidGrade = (grade >= 0.0 && grade <= 100.0);
		if (!isValidGrade) {
			throw new ApiException("Invalid grade: " + grade);
		}


		// 2. create grade entity & save to DB
		return (T) switch (subject) {
			case HISTORY -> createGradeInternal(grade, foundStudent, HistoryGrade::new, historyGradeDao);
			case MATH -> createGradeInternal(grade, foundStudent, MathGrade::new, mathGradeDao);
			case SCIENCE -> createGradeInternal(grade, foundStudent, ScienceGrade::new, scienceGradeDao);
			default -> throw new ApiException("Invalid subject: " + subject);
		};
	}

	public <T extends Grade> int deleteGrade(int gradeId, Grade.Subject subject) {
		GradeDao<T> gradeDao = getDaoBySubject(subject);
		T grade = gradeDao.findById(gradeId)
				.orElseThrow(() -> new GradeNotFoundException(String.format("%s#%d not found", subject.getEntityClass().getSimpleName(), gradeId)));
		gradeDao.delete(grade);

		return grade.getCollegeStudent().getId();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public CollegeStudent findStudentWithGrades(int studentId) {
		CollegeStudent found = studentDao.findById(studentId)
				.orElseThrow(() -> new StudentNotFoundException("Student#" + studentId + " not found"));

		found.getHistoryGrades().size();
//		Hibernate.initialize(found.getHistoryGrades());
		found.getMathGrades().size();
		found.getScienceGrades().size();

		return found;
	}


	// domain mapping methods
	// fixme: not tested
	public List<CollegeStudentDto> findAllStudentDtos() {
		List<CollegeStudentDto> dtos = new ArrayList<>();
		for (CollegeStudent cs : studentDao.findAll()) {
			dtos.add(new CollegeStudentDto(
					cs.getId(),
					cs.getFirstname(),
					cs.getLastname(),
					cs.getEmailAddress()
			));
		}
		return dtos;
	}


	// helper methods
	private boolean isStudentFound(int id) {
		return studentDao.existsById(id);
	}

	private boolean isGradeFound(int id, @NotNull Grade.Subject subject) {
		if (id <= 0)
			return false;

		return switch (subject) {
			case MATH ->  this.mathGradeDao.existsById(id);
			case HISTORY ->  this.historyGradeDao.existsById(id);
			case SCIENCE ->  this.scienceGradeDao.existsById(id);
			default -> throw new ApiException("Invalid subject: " + subject);
		};
	}

	@SuppressWarnings("unchecked")
	private <T extends Grade> GradeDao<T> getDaoBySubject(Grade.Subject type) {
		return (GradeDao<T>) switch (type) {
			case HISTORY -> historyGradeDao;
			case MATH -> mathGradeDao;
			case SCIENCE -> scienceGradeDao;
			default -> throw new ApiException("Invalid type: " + type);
		};
	}

	private <T extends Grade> T createGradeInternal(
			double grade,
			CollegeStudent student,
			Supplier<T> factory,
			GradeDao<T> gradeDao
	) {
		T newGrade = factory.get();
		newGrade.setGrade(grade);
		student.associate(newGrade);
		return gradeDao.save(newGrade);
	}
}