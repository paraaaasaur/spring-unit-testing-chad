package com.herbivore.springmvc;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.GradebookCollegeStudent;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {
	private final JdbcTemplate jdbcTemplate;
	private final MockMvc mockMvc;
	@Mock
	private StudentAndGradeService studentAndGradeServiceMock;

	@Autowired
	public GradebookControllerTest(JdbcTemplate jdbcTemplate, MockMvc mockMvc) {
		this.jdbcTemplate = jdbcTemplate;
		this.mockMvc = mockMvc;
	}


	@BeforeEach
	void setup() {
		final String sql = """
				INSERT INTO student
				(firstname, lastname, email_address)
				VALUES ('Tom', 'Riddle', 'hi-im-tom@gmail.com')""";
		jdbcTemplate.execute(sql);
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

	@DisplayName("Test Service Mock")
	@Test
	void testServiceMock() throws Exception {
		// prepare stubs
		CollegeStudent studentOne = new GradebookCollegeStudent("Tom", "Riddle", "hi-im-tom@gmail.com");
		CollegeStudent studentTwo = new GradebookCollegeStudent("John", "Doe", "jd@gmail.com");
		List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

		// prepare mock scenario
		when(studentAndGradeServiceMock.getGradebook())
				.thenReturn(collegeStudentList);

		Iterable<CollegeStudent> expected = collegeStudentList;
		Iterable<CollegeStudent> actual = studentAndGradeServiceMock.getGradebook();

		assertIterableEquals(expected, actual);
	}
}