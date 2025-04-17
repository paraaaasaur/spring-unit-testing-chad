package com.herbivore.springmvc;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.MathGrade;
import com.herbivore.springmvc.repository.MathGradeDao;
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
import java.util.List;
import java.util.Optional;

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


	@Autowired
	protected StudentAndGradeServiceTest(StudentAndGradeService studentService, StudentDao studentDao, JdbcTemplate jdbcTemplate, MathGradeDao mathGradeDao) {
		this.studentService = studentService;
		this.studentDao = studentDao;
		this.jdbcTemplate = jdbcTemplate;
		this.mathGradeDao = mathGradeDao;
	}


	@BeforeEach
	void setUpDatabase() {
		final String sql = """
				INSERT INTO student
				(firstname, lastname, email_address)
				VALUES ('Tom', 'Riddle', 'hi-im-tom@gmail.com')""";
		jdbcTemplate.execute(sql);
//		studentService.createStudent("Tom", "Riddle", "hi-im-tom@gmail.com");
	}

	@AfterEach
	void cleanUpAfterTransaction() {
		final String deleteSql = "DELETE FROM student";
		final String resetIdSql = """
				ALTER TABLE student
				ALTER COLUMN id RESTART WITH 1""";

		jdbcTemplate.execute(deleteSql);
		jdbcTemplate.execute(resetIdSql);
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
	void checkIfStudentIsNull() {
		assertTrue(studentService.checkIfStudentIsNull(1));

		assertFalse(studentService.checkIfStudentIsNull(0));
	}

	@Test
	void testQueryAnnotation() {
		var students = studentDao.findByEmailAddressLike("%gmail%");
		System.out.println(students.getClass());
		students.forEach(System.out::println);
	}

	@DisplayName("TTD for Service#Delete-Student")
	@Test
	void deleteStudentService() {
		Optional<CollegeStudent> studentOp = studentDao.findById(1);

		assertTrue(studentOp.isPresent(), "Student#1 should exist");

		studentService.deleteStudent(1);

		studentOp = studentDao.findById(1);

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
		assertTrue(studentService.createGrade(80.50, 1, "math"));

		// Get all grades with studentId
		Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);

		// Verify there are grades
		System.out.println(mathGrades);
		assertTrue(mathGrades.iterator().hasNext(), "Student#1 has math grade");
	}
}
