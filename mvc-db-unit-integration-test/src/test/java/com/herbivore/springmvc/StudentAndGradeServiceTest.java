package com.herbivore.springmvc;

import com.herbivore.springmvc.model.CollegeStudent;
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


	@Autowired
	protected StudentAndGradeServiceTest(StudentAndGradeService studentService, StudentDao studentDao, JdbcTemplate jdbcTemplate) {
		this.studentService = studentService;
		this.studentDao = studentDao;
		this.jdbcTemplate = jdbcTemplate;
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

	@DisplayName("TTD for Service#Delete-Student")
	@Test
	void deleteStudentService() {
		Optional<CollegeStudent> studentOp = studentDao.findById(1);

		assertTrue(studentOp.isPresent(), "Student#1 should exist");

		studentService.deleteStudent(1);

		studentOp = studentDao.findById(1);

		assertFalse(studentOp.isPresent(), "Student#1 should've been deleted");
	}
}
