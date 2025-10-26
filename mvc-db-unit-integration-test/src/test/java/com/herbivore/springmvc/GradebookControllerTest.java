package com.herbivore.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
	void placeHolder() {}
}