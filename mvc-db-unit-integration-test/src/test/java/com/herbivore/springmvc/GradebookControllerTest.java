package com.herbivore.springmvc;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
	private final StudentAndGradeService studentService;
	private final MathGradeDao mathGradeDao;

	@Value("${sql.script.create.student}")
	private String createStudentSql;
	@Value("${sql.script.create.grade.history}")
	private String createHistoryGradeSql;
	@Value("${sql.script.create.grade.math}")
	private String createMathGradeSql;
	@Value("${sql.script.create.grade.science}")
	private String createScienceGradeSql;
	@Value("${sql.script.delete.student}")
	private String deleteStudentSql;
	@Value("${sql.script.delete.grade.history}")
	private String deleteHistoryGradeSql;
	@Value("${sql.script.delete.grade.math}")
	private String deleteMathGradeSql;
	@Value("${sql.script.delete.grade.science}")
	private String deleteScienceGradeSql;


	@Autowired
	public GradebookControllerTest(JdbcTemplate jdbcTemplate, MockMvc mockMvc, StudentDao studentDao, StudentAndGradeService studentService, MathGradeDao mathGradeDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.mockMvc = mockMvc;
		this.studentDao = studentDao;
		this.studentService = studentService;
		this.mathGradeDao = mathGradeDao;
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
		jdbcTemplate.execute(createStudentSql);
		jdbcTemplate.execute(createHistoryGradeSql);
		jdbcTemplate.execute(createMathGradeSql);
		jdbcTemplate.execute(createScienceGradeSql);
	}

	@AfterEach
	void cleanUpAfterTransaction() {
		jdbcTemplate.execute(deleteStudentSql);
		jdbcTemplate.execute(deleteHistoryGradeSql);
		jdbcTemplate.execute(deleteMathGradeSql);
		jdbcTemplate.execute(deleteScienceGradeSql);
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
						.contentType(APPLICATION_JSON)
						.param("firstname", "John")
						.param("lastname", "Doe")
						.param("emailAddress", "jd@gmail.com"))
				.andExpect(status().is3xxRedirection())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "redirect:/");


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

	@DisplayName("TDD for #studentInformation Happy Route")
	@Test
	void studentInformationHttpRequest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/studentInformation/{id}", 1))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
	}

	@DisplayName("TDD for #studentInformation Error Route")
	@Test
	void studentInformationHttpStudentDoesNotExistRequest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/studentInformation/{id}", 0))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "error");
	}

	/**
	 * 1. POST /grades endpoint existence<br>
	 * 2. POST /grades functionality: creates grade based on request params<br>
	 * 3. Redirect to... TODO
	 **/
	@DisplayName("TTD for POST /grades View Resolution & Functionality")
	@Test
	void createValidGradeHttpRequest() throws Exception {
		// 0. verify initial
		assertTrue(studentDao.findById(1).isPresent());

		var gcs = studentService.studentInformation(1);
		int actualSize = gcs.getStudentGrades().getMathGradeResults().size();
		assertEquals(1, actualSize);


		// 1. pathway & functionality check
		MvcResult mvcResult = mockMvc
				.perform(post("/grades")
						.contentType(APPLICATION_JSON)
						.param("gradeType", "MATH")
						.param("grade", "85.00")
						.param("studentId", "1"))
				.andExpect(status().is3xxRedirection())
				.andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "redirect:/studentInformation/1");


		// 2. verify after
		gcs = studentService.studentInformation(1);
		actualSize = gcs.getStudentGrades().getMathGradeResults().size();
		assertEquals(2, actualSize);
	}

	@DisplayName("POST /grades w/ Invalid Student ID")
	@Test
	void createValidGradeHttpRequestStudentDoesNotExistEmptyResponse() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/grades")
						.contentType(APPLICATION_JSON)
						.param("gradeType", "HISTORY")
						.param("grade", "95.00")
						.param("studentId", "0"))
				.andExpect(status().isOk())
				.andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@DisplayName("POST /grades w/ Invalid Grade Type")
	@Test
	void createInvalidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/grades")
						.contentType(APPLICATION_JSON)
						.param("gradeType", "history")
						.param("grade", "95.00")
						.param("studentId", "1"))
				.andExpect(status().isBadRequest())
				.andReturn();

//		System.out.println(">>>>>>> " + mvcResult.getResolvedException());
	}

	@DisplayName("TTD for Controller#Delete-Grade")
	@Test
	void deleteValidGradeHttpRequest() throws Exception {
		// 1. verify initial
		assertTrue(mathGradeDao.findById(1).isPresent());

		// 2. pathway check
		MvcResult mvcResult = mockMvc
				// which invokes Service#deleteGrade()
				.perform(post("/grades/{id}/{gradeType}", "1", "MATH"))
				.andExpect(status().is3xxRedirection())
				.andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "redirect:/studentInformation/1");

		// 3. functionality logic check
		assertFalse(mathGradeDao.findById(1).isPresent());
	}

	/** Grade doesn't exist -> neither does studentId (based on the contract)*/
	@DisplayName("Controller#Delete-Grade w/ Invalid Grade ID")
	@Test
	void deleteByInvalidGradeIdHttpRequestResolvesErrorPage() throws Exception {
		// 1. verify initial
		assertFalse(mathGradeDao.findById(-1).isPresent());

		// 2. pathway check
		MvcResult mvcResult = mockMvc
				.perform(post("/grades/{id}/{gradeType}", "-1", "MATH"))
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
