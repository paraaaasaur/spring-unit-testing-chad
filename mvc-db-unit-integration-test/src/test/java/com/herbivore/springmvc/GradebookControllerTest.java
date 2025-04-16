package com.herbivore.springmvc;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {
	private final JdbcTemplate jdbcTemplate;
	private final MockMvc mockMvc;
	@Mock
	private StudentAndGradeService serviceMock;
	private static MockHttpServletRequest requestMock;
	private final StudentDao studentDao;


	@Autowired
	public GradebookControllerTest(JdbcTemplate jdbcTemplate, MockMvc mockMvc, StudentDao studentDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.mockMvc = mockMvc;
		this.studentDao = studentDao;
	}

	// reminder: @BeforeAll methods are always public static void
	@BeforeAll
	public static void beforeAll() {
		// may use static block alternatively, because
		// JUnit lifecycle isn't relevant for this case
		{
			requestMock = new MockHttpServletRequest();
			requestMock.setParameter("firstname", "John");
			requestMock.setParameter("lastname", "Doe");
			requestMock.setParameter("emailAddress", "jd@gmail.com");
		}
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

	/**
	 * <h3>MockMvc Workflow Scenario:</h3>
	 * 1. GET request | expects HTTP 200 OK<br>
	 * 2. View resolution | verifies: the view returned == "index.html"<br>
	 **/
	@Test
	public void getStudentsHttpRequest () throws Exception {
		// 1. GET request | expects HTTP 200 OK
		MvcResult mvcResult = mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andReturn();

		// 2. View resolution | verifies: the view returned == "index.html"
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
	}

	/**
	 * <h3>MockMvc Workflow Scenario: POST-create student</h3>
	 * 1. POST: Application/JSON(content-type) + params<br>
	 * 2. View resolution | verifies: the view returned == "index.html"<br>
	 **/
	@DisplayName("TDD for POST-Create Student Endpoint")
	@Test
	void createStudentHttpRequest() throws Exception {
		// 1. Pathway check: from entry(request) to exit("index.html")
		MvcResult mvcResult = mockMvc.perform(post("/")
						.contentType(MediaType.APPLICATION_JSON)
						.param("firstname", "John")
						.param("lastname", "Doe")
						.param("emailAddress", "jd@gmail.com"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");


		// 2. Functionality check: create student
		CollegeStudent dbStudent = studentDao
				.findByEmailAddress(requestMock.getParameter("emailAddress"));
		assertNotNull(dbStudent, "Student should've been created");
	}

	/**
	 * <h3>MockMvc Workflow Scenario: POST-delete student</h3>
	 * 1. POST: PathVariable<br>
	 * 2. View resolution | verifies: the view returned == "index.html"<br>
	 **/
	@DisplayName("TDD for POST-Delete Student Endpoint")
	@Test
	void deleteStudentHttpRequest() throws Exception {
		// 0. Sanity check: "We do have test data#1 from @BeforeEach, right...?"
		assertTrue(studentDao.findById(1).isPresent());


		// 1. Pathway check
		final String endpoint = "/delete/student/{id}";
		MvcResult mvcResult = mockMvc.perform(post(endpoint, 1))
				.andExpect(status().is3xxRedirection())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "redirect:/");


		// 2. Functionality check
		boolean condition = studentDao.findById(1).isPresent();
		assertFalse(condition, "Student should've been deleted");
	}

	@DisplayName("TDD for Error Page Route If deleted Student Doesn't Exist")
	@Test
	void deleteStudentHttpRequestErrorPage() throws Exception {
		final String endpoint = "/delete/student/{id}";
		MvcResult mvcResult = mockMvc.perform(post(endpoint, 0))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "error");
	}

	private class Archived {
		//		@DisplayName("Test Service Mock")
//		@Test
		void testServiceMock() {
			// prepare stubs
			CollegeStudent studentOne = new CollegeStudent("Tom", "Riddle", "hi-im-tom@gmail.com");
			CollegeStudent studentTwo = new CollegeStudent("John", "Doe", "jd@gmail.com");
			List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

			// prepare mock scenario
			when(serviceMock.getGradebook())
					.thenReturn(collegeStudentList);

			Iterable<CollegeStudent> expected = collegeStudentList;
			Iterable<CollegeStudent> actual = serviceMock.getGradebook();

			assertIterableEquals(expected, actual);
		}
	}
}
