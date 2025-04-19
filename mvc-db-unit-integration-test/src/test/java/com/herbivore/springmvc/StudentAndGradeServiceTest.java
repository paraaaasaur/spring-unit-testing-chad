package com.herbivore.springmvc;

import com.herbivore.springmvc.model.*;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.herbivore.springmvc.model.Grade.Type.*;
import static org.junit.jupiter.api.Assertions.*;

// rn just use the regular one
// can switch to test-dedicated one in the future
@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {
	private final StudentAndGradeService studentService;
	private final StudentDao studentDao;
	private final JdbcTemplate jdbcTemplate;
	private final MathGradeDao mathGradeDao;
	private final ScienceGradeDao scienceGradeDao;
	private final HistoryGradeDao historyGradeDao;


	@Autowired
	protected StudentAndGradeServiceTest(StudentAndGradeService studentService, StudentDao studentDao, JdbcTemplate jdbcTemplate, MathGradeDao mathGradeDao, ScienceGradeDao scienceGradeDao, HistoryGradeDao historyGradeDao) {
		this.studentService = studentService;
		this.studentDao = studentDao;
		this.jdbcTemplate = jdbcTemplate;
		this.mathGradeDao = mathGradeDao;
		this.scienceGradeDao = scienceGradeDao;
		this.historyGradeDao = historyGradeDao;
	}


	@BeforeEach
	void setUpDatabase() {
		final String sql = """
				INSERT INTO student
				(firstname, lastname, email_address)
				VALUES ('Tom', 'Riddle', 'hi-im-tom@gmail.com')""";
		jdbcTemplate.execute(sql);
//		studentService.createStudent("Tom", "Riddle", "hi-im-tom@gmail.com");

		jdbcTemplate.execute("INSERT INTO math_grade (student_id, grade) " +
							 "VALUES (1, 100.00)");
		jdbcTemplate.execute("INSERT INTO science_grade (student_id, grade) " +
							 "VALUES (1, 100.00)");
		jdbcTemplate.execute("INSERT INTO history_grade (student_id, grade) " +
							 "VALUES (1, 100.00)");
	}

	@AfterEach
	void cleanUpAfterTransaction() {
		final String deleteSql = "DELETE FROM student";
		final String resetIdSql = """
				ALTER TABLE student
				ALTER COLUMN id RESTART WITH 1""";

		jdbcTemplate.execute(deleteSql);
		jdbcTemplate.execute(resetIdSql);

		jdbcTemplate.execute("DELETE FROM math_grade");
		jdbcTemplate.execute("ALTER TABLE math_grade ALTER COLUMN id RESTART WITH 1");
		jdbcTemplate.execute("DELETE FROM science_grade");
		jdbcTemplate.execute("ALTER TABLE science_grade ALTER COLUMN id RESTART WITH 1");
		jdbcTemplate.execute("DELETE FROM history_grade");
		jdbcTemplate.execute("ALTER TABLE history_grade ALTER COLUMN id RESTART WITH 1");

	}

	@DisplayName("TTD for Service & DAO")
	@Test
	void createStudentService() {
		CollegeStudent student = studentDao.findByEmailAddress("hi-im-tom@gmail.com");

		String expected = "hi-im-tom@gmail.uk";
//		String expected = "hi-im-tom@gmail.com";
		String actual = student.getEmailAddress();

		assertEquals(expected, actual, "find by email");
	}

	@DisplayName("TTD for Service#Null-Check")
	@Test
	void isStudentFound() {
		assertTrue(studentService.isStudentFound(1));

		assertFalse(studentService.isStudentFound(0));
	}

	@Test
	void testQueryAnnotation() {
		var students = studentDao.findByEmailAddressLike("%gmail%");
		System.out.println(students.getClass());
		students.forEach(System.out::println);
	}

	/**
	 * 1. Delete a student<br>
	 * 2. Establish delete-on-cascade so that deletes
	 *    all grades from the student as well<br>
	 **/
	@DisplayName("TTD for Service#Delete-Student")
	@Test
	void deleteStudentService() {
		Optional<CollegeStudent> studentOp = studentDao.findById(1);

		assertTrue(studentOp.isPresent(), "Student#1 should exist");

		studentService.deleteStudent(1);

		studentOp = studentDao.findById(1);

		assertFalse(historyGradeDao.findGradeByStudentId(1).iterator().hasNext());
		assertFalse(mathGradeDao.findGradeByStudentId(1).iterator().hasNext());
		assertFalse(scienceGradeDao.findGradeByStudentId(1).iterator().hasNext());

		assertFalse(studentOp.isPresent(), "Student#1 should've been deleted");
	}

	@Sql("/insert-data.sql")
	@DisplayName("TTD for Service#Get-GradeBook")
	@Test
	void getGradeBookService() {
		Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

		List<CollegeStudent> collegeStudents = new ArrayList<>();

		for (var cs : iterableCollegeStudents) {
			collegeStudents.add(cs);
		}

		assertEquals(5, collegeStudents.size());
	}

	// CH9: Create grade service

	@DisplayName("TTD for Grade Functionality")
	@Test
	void createGradeService() {

		// Create the grade
		assertTrue(studentService.createGrade(80.50, 1, MATH));
		assertTrue(studentService.createGrade(80.50, 1, SCIENCE));
		assertTrue(studentService.createGrade(80.50, 1, HISTORY));

		// Get all grades with studentId
		Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
		Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
		Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

		// Verify there are grades
		assertTrue(((Collection<MathGrade>)mathGrades).size() == 2, "Student#1 has math grade");
		assertTrue(((Collection<ScienceGrade>)scienceGrades).size() == 2, "Student#1 has science grade");
		assertTrue(((Collection<HistoryGrade>)historyGrades).size() == 2, "Student#1 has history grade");
	}

	@DisplayName("Test Edge Cases for Creating Grades")
	@Test
	void createGradeServiceReturnFalse() {
		// false grade
		assertFalse(studentService.createGrade(100.5, 1, MATH));
		assertFalse(studentService.createGrade(-5.5, 1, MATH));
		assertFalse(studentService.createGrade(Double.NaN, 1, MATH));
		assertFalse(studentService.createGrade(Double.POSITIVE_INFINITY, 1, MATH));
		assertFalse(studentService.createGrade(Double.NEGATIVE_INFINITY, 1, MATH));

		// different ids
		assertFalse(studentService.createGrade(80.5, 2, MATH));

		// false subject
		assertFalse(studentService.createGrade(100.5, 1, UNKNOWN));
	}

	@DisplayName("TDD for GradeService#delete-grade")
	@Test
	void deleteGradeService() {
		// Return student id after deletion
		int studentIdFromMath = studentService.deleteGrade(1, MATH);
		assertEquals(1, studentIdFromMath);

		int studentIdFromScience = studentService.deleteGrade(1, SCIENCE);
		assertEquals(1, studentIdFromScience);

		int studentIdFromHistory = studentService.deleteGrade(1, HISTORY);
		assertEquals(1, studentIdFromHistory);
	}

	@DisplayName("Edge Cases: Invalid Grade ID for Deleting Grades")
	@Test
	void deleteGradeServiceReturnStudentIdOfZero() {
		assertEquals(0, studentService.deleteGrade(-1, MATH));
	}

	@DisplayName("Retrieve Student Information")
	@Test
	void studentInformation() {
		GradesAndCollegeStudent gcs = studentService.studentInformation(1);

		assertNotNull(gcs);
		assertEquals(1, gcs.getCollegeStudent().getId());
		assertEquals("Tom", gcs.getCollegeStudent().getFirstname());
		assertEquals("Riddle", gcs.getCollegeStudent().getLastname());
		assertEquals("hi-im-tom@gmail.com", gcs.getCollegeStudent().getEmailAddress());
		assertEquals(1, gcs.getStudentGrades().getHistoryGradeResults().size());
		assertEquals(1, gcs.getStudentGrades().getMathGradeResults().size());
		assertEquals(1, gcs.getStudentGrades().getScienceGradeResults().size());
	}

	@DisplayName("Edge Case for #studentInformation: Invalid Student")
	@Test
	void studentInformationServiceReturnNull() {
		GradesAndCollegeStudent gcs = studentService.studentInformation(0);

		assertNull(gcs);
	}
}
