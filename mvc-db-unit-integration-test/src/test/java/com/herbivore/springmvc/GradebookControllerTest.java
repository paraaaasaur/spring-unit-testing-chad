package com.herbivore.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
//@Transactional // for entity manager
class GradebookControllerTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // new/injection choose one

	private final EntityManager em;
	private final JdbcTemplate jdbcTemplate;
	private final MockMvc mockMvc;

	private final StudentAndGradeService studentAndGradeService;
	private final StudentDao studentDao;
	private final HistoryGradeDao historyGradeDao;
	private final MathGradeDao mathGradeDao;
	private final ScienceGradeDao scienceGradeDao;

	@Mock
	private final StudentAndGradeService  studentAndGradeServiceMock;

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
	GradebookControllerTest(EntityManager em, JdbcTemplate jdbcTemplate, MockMvc mockMvc, StudentAndGradeService studentAndGradeService, StudentDao studentDao, HistoryGradeDao historyGradeDao, MathGradeDao mathGradeDao, ScienceGradeDao scienceGradeDao, StudentAndGradeService studentAndGradeServiceMock) {
		this.em = em;
		this.jdbcTemplate = jdbcTemplate;
		this.mockMvc = mockMvc;
		this.studentAndGradeService = studentAndGradeService;
		this.studentDao = studentDao;
		this.historyGradeDao = historyGradeDao;
		this.mathGradeDao = mathGradeDao;
		this.scienceGradeDao = scienceGradeDao;
		this.studentAndGradeServiceMock = studentAndGradeServiceMock;
	}

	@BeforeEach
	void setupDatabase() {
//		em.createNativeQuery(createStudentSql).executeUpdate();
		jdbcTemplate.execute(createStudentSql);
		jdbcTemplate.execute(createHistoryGradeSql);
		jdbcTemplate.execute(createMathGradeSql);
		jdbcTemplate.execute(createScienceGradeSql);
	}

	@AfterEach
	void teardownAfterTx() {
		jdbcTemplate.execute(deleteHistoryGradeSql);
		jdbcTemplate.execute(deleteMathGradeSql);
		jdbcTemplate.execute(deleteScienceGradeSql);
		jdbcTemplate.execute(deleteStudentSql);
	}

	@Test
	@Transactional(readOnly = true)
	void whenGetStudents_ThenReturnStudentsWithGrades() throws Exception {
		var cs = new CollegeStudent("Paris", "Jackson", "PK@gmail.com");
		em.setFlushMode(FlushModeType.COMMIT);
		em.persist(cs);
//		em.flush();

		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1 + 1)))
				.andExpect(jsonPath("$[0].historyGrades", hasSize(1)))
				.andExpect(jsonPath("$[0].historyGrades[0].grade", is(100.0)));
	}

	@Test
	void whenCreateStudent_thenReturnStudentWithoutGrades() throws Exception {
		CollegeStudent rawReqBody = new CollegeStudent("Mr", "Poring", "imsocuteayaya@ragnarok.ko");
		String reqBody = OBJECT_MAPPER.writeValueAsString(rawReqBody);
		System.out.println(reqBody);

		mockMvc.perform(post("/student")
					.contentType(MediaType.APPLICATION_JSON)
					.content(reqBody))
					// trap: params are bad request for application/json.
					// they are x-www-urlencoded
//					.param("firstname", "Mr")
//					.param("lastname", "Poring")
//					.param("emailAddress", "pf@ragnarok.ko"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].lastname", is("Poring")));

		assertTrue(studentDao.existsByEmailAddress("imsocuteayaya@ragnarok.ko"));
	}

	@Test
	void whenDeleteStudent_thenReturnAllStudentDtos() throws Exception {
		assertTrue(studentDao.existsById(1));

		mockMvc.perform(delete("/student/{id}", 1))
				.andExpect(status().isNoContent())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(content().string("[]"))
//				.andExpect(content().encoding("UTF-8"))
				.andExpect(jsonPath("$", hasSize(0)));

		assertFalse(studentDao.existsById(1));
	}

	@Test
	void whenStudentInformation_thenReturnStudentWithGrades() throws Exception {
		assertTrue(studentDao.existsById(1));

		mockMvc.perform(get("/studentInformation/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstname", is("Tom")))
				.andExpect(jsonPath("$.lastname", is("Riddle")))
				.andExpect(jsonPath("$.emailAddress", is("hi-im-tom@gmail.com")))
				.andExpect(jsonPath("$.historyGrades[0].id", is(1)))
				.andExpect(jsonPath("$.historyGrades[0].grade", is(100.0)))
				.andExpect(jsonPath("$.mathGrades[0].grade", is(100.0)))
				.andExpect(jsonPath("$.scienceGrades[0].grade", is(100.0)));
	}

	@Test
	void givenInvalidId_whenStudentInformation_thenTriggersStudentNotFoundException() throws Exception {
		assertFalse(studentDao.existsById(0));

		mockMvc.perform(get("/studentInformation/{id}", 0))
				// behaviors from GlobalErrorhandler#handleStudentNotFound
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.detail", is("Student#0 not found")));
	}
}