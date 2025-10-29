package com.herbivore.springmvc;

import com.herbivore.springmvc.exception.ApiException;
import com.herbivore.springmvc.exception.GradeNotFoundException;
import com.herbivore.springmvc.exception.StudentNotFoundException;
import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.HistoryGrade;
import com.herbivore.springmvc.model.MathGrade;
import com.herbivore.springmvc.model.ScienceGrade;
import com.herbivore.springmvc.repository.HistoryGradeDao;
import com.herbivore.springmvc.repository.MathGradeDao;
import com.herbivore.springmvc.repository.ScienceGradeDao;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static com.herbivore.springmvc.model.Grade.Subject.*;
import static io.github.paraaaasaur.util.Toolbox.hl;
import static org.junit.jupiter.api.Assertions.*;

//@TestPropertySource("/application-test.properties") // fine-tuner; tweaks or injects specific properties for the test context.
@ActiveProfiles("test") // for testing environment-specific behavior
@SpringBootTest
class StudentAndGradeServiceTest {
	private final StudentAndGradeService studentService;
	private final StudentDao studentDao;
	private final JdbcTemplate jdbcTemplate;
	private final MathGradeDao mathGradeDao;
	private final ScienceGradeDao scienceGradeDao;
	private final HistoryGradeDao historyGradeDao;

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
	StudentAndGradeServiceTest(StudentAndGradeService studentService, StudentDao studentDao, JdbcTemplate jdbcTemplate, MathGradeDao mathGradeDao, ScienceGradeDao scienceGradeDao, HistoryGradeDao historyGradeDao) {
		this.studentService = studentService;
		this.studentDao = studentDao;
		this.jdbcTemplate = jdbcTemplate;
		this.mathGradeDao = mathGradeDao;
		this.scienceGradeDao = scienceGradeDao;
		this.historyGradeDao = historyGradeDao;
	}

	@BeforeEach
	void setUpDatabase() {
		jdbcTemplate.execute(createStudentSql);
		jdbcTemplate.execute(createHistoryGradeSql);
		jdbcTemplate.execute(createMathGradeSql);
		jdbcTemplate.execute(createScienceGradeSql);
	}

	@AfterEach
	void cleanUpAfterTransaction() {
		jdbcTemplate.execute(deleteHistoryGradeSql);
		jdbcTemplate.execute(deleteMathGradeSql);
		jdbcTemplate.execute(deleteScienceGradeSql);
		jdbcTemplate.execute(deleteStudentSql);
	}

	@Test
	void createStudentService() {
		String expected = "tatsunoko@hololive.tv";

		studentService.createStudent("Sorimasen", "Chunks", expected);
		String actual = studentDao.findByEmailAddress(expected).getEmailAddress();

		assertEquals(expected, actual, "Found student's email is " + expected);
	}

	@Test // extra
	void testQueryAnnotation() {
		var students = studentDao.findByEmailAddressLike("%gmail%");
		System.out.println(students.getClass());
		students.forEach(System.out::println);
	}

	@Test
	void deleteStudentService() {
		var studentOp = studentDao.findById(1);

		assertTrue(studentOp.isPresent(), "Student#1 should exist");

		studentService.deleteStudent(1);

		studentOp = studentDao.findById(1);

		assertFalse(historyGradeDao.findAllByCollegeStudentId(1).iterator().hasNext());
		assertFalse(mathGradeDao.findAllByCollegeStudentId(1).iterator().hasNext());
		assertFalse(scienceGradeDao.findAllByCollegeStudentId(1).iterator().hasNext());

		assertFalse(studentOp.isPresent(), "Student#1 should've been deleted");
	}

	@SqlGroup({
			@Sql(scripts = "/insert-student.sql", config = @SqlConfig(commentPrefix = "`")),
			@Sql("/override-data.sql"),
			@Sql("/insert-grade.sql")}
	)
	@Test
	void findAllStudentsWithGrades() {
		List<CollegeStudent> csList = studentService.findAllStudentsWithGrades();

		List<CollegeStudent> csListTest = csList.stream()
				.filter(cs -> cs.getId() > 10)
				.toList();

		assertEquals(4, csListTest.size());

		var collegeStudentIdx2 =  csListTest.get(2);
		var csIdIdx2 = collegeStudentIdx2.getId();

		assertIterableEquals(collegeStudentIdx2.getHistoryGrades(), historyGradeDao.findAllByCollegeStudentId(csIdIdx2));
		assertIterableEquals(collegeStudentIdx2.getMathGrades(), mathGradeDao.findAllByCollegeStudentId(csIdIdx2));
		assertIterableEquals(collegeStudentIdx2.getScienceGrades(), scienceGradeDao.findAllByCollegeStudentId(csIdIdx2));

		assertEquals(collegeStudentIdx2, studentDao.findById(csIdIdx2).orElse(null));


		var collegeStudentIdx0 = csListTest.get(0);
		assertNotNull(collegeStudentIdx0.getHistoryGrades());
		assertNotNull(collegeStudentIdx0.getScienceGrades());
		assertNotNull(collegeStudentIdx0.getMathGrades());
	}

	@Test
	void createGradeService() {
		assertTrue(studentDao.existsById(1));

		// Create the grade
		assertNotNull(studentService.createGrade(80.50, 1, MATH));
		assertNotNull(studentService.createGrade(80.50, 1, SCIENCE));
		assertNotNull(studentService.createGrade(80.50, 1, HISTORY));
		assertThrows(ApiException.class, () -> studentService.createGrade(80.50, 1, UNKNOWN));


		// Get all grades with studentId
		var mathGrades = mathGradeDao.findAllByCollegeStudentId(1);
		var scienceGrades = scienceGradeDao.findAllByCollegeStudentId(1);
		var historyGrades = historyGradeDao.findAllByCollegeStudentId(1);

		// Verify there are grades
		assertTrue((mathGrades).iterator().hasNext(), "Student#2 has MATH grade");
		assertTrue((scienceGrades).iterator().hasNext(), "Student#2 has SCIENCE grade");
		assertTrue((historyGrades).iterator().hasNext(), "Student#2 has HISTORY grade");
	}

	@Test // omitted by Chad
	void createGradeServiceReturnFalse() {
		// false grade
		assertThrows(ApiException.class, () -> studentService.createGrade(100.5, 1, MATH));
		assertThrows(ApiException.class, () -> studentService.createGrade(-5.5, 1, MATH));
		assertThrows(ApiException.class, () -> studentService.createGrade(Double.NaN, 1, MATH));
		assertThrows(ApiException.class, () -> studentService.createGrade(Double.POSITIVE_INFINITY, 1, MATH));
		assertThrows(ApiException.class, () -> studentService.createGrade(Double.NEGATIVE_INFINITY, 1, MATH));

		// invalid student id
		assertThrows(StudentNotFoundException.class, () -> studentService.createGrade(80.5, 2, MATH));

		// false subject
		assertThrows(ApiException.class, () -> studentService.createGrade(58.6, 1, UNKNOWN));
	}

	@Test
	void deleteGradeService() {
		// #deleteGrade returns student id; 0 for failed deletion
		assertEquals(1, studentService.deleteGrade(1, MATH),
				"Student#1 has MATH grade created in @BeforeEach");

		assertEquals(1, studentService.deleteGrade(1, SCIENCE),
				"Student#1 has SCIENCE grade created in @BeforeEach");

		assertEquals(1, studentService.deleteGrade(1, HISTORY),
				"Student#1 has HISTORY grade created in @BeforeEach");

		assertThrows(GradeNotFoundException.class,
				() -> studentService.deleteGrade(0, SCIENCE),
				"Student#0 indicates failed deletion");

		assertThrows(ApiException.class,
				() -> studentService.deleteGrade(1, UNKNOWN),
				"Student#0 indicates failed deletion");
	}

	@Test
	void findStudentWithGrades() {
		CollegeStudent foundCs = studentService.findStudentWithGrades(1);

		assertNotNull(foundCs);
		assertEquals(1, foundCs.getId());
		assertEquals("Tom", foundCs.getFirstname());
		assertEquals("Riddle", foundCs.getLastname());
		assertEquals("hi-im-tom@gmail.com", foundCs.getEmailAddress());
		assertEquals(1, foundCs.getHistoryGrades().size());
		assertEquals(1, foundCs.getMathGrades().size());
		assertEquals(1, foundCs.getScienceGrades().size());
	}

	@Test
	void findStudentWithGrades_shouldReturnNull_whenUserDoesNotExist() {
		Executable exe = () -> studentService.findStudentWithGrades(0);

		assertThrows(StudentNotFoundException.class, exe);
	}

	@Test // extra
	void associateAndDissociate() {
		var newCs = new CollegeStudent("Chili", "Pasta", "cp@gmail.it");
		var newHg = new HistoryGrade(17.5);
		var newMg = new MathGrade(18.5);
		var newSci = new ScienceGrade(19.5);
		newCs.associate(newHg);
		newCs.associate(newMg);
		newCs.associate(newSci);

		int id = studentDao.save(newCs).getId();
		var foundCs = studentService.findStudentWithGrades(id);
		System.out.println(foundCs);
		System.out.println(foundCs.getHistoryGrades());
		System.out.println(foundCs.getMathGrades());
		System.out.println(foundCs.getScienceGrades());

		assertEquals(1, foundCs.getHistoryGrades().size());
		assertEquals(1, foundCs.getMathGrades().size());
		assertEquals(1, foundCs.getScienceGrades().size());

		hl();

		foundCs.dissociate(foundCs.getHistoryGrades().iterator().next());
		foundCs.dissociate(foundCs.getMathGrades().iterator().next());
		foundCs.dissociate(foundCs.getScienceGrades().iterator().next());


		studentDao.save(foundCs);

		hl();

		var foundCs2 = studentService.findStudentWithGrades(id);
		System.out.println(foundCs2);
		System.out.println(foundCs2.getHistoryGrades());
		System.out.println(foundCs2.getMathGrades());
		System.out.println(foundCs2.getScienceGrades());

		assertTrue(foundCs2.getHistoryGrades().isEmpty());
		assertTrue(foundCs2.getMathGrades().isEmpty());
		assertTrue(foundCs2.getScienceGrades().isEmpty());
	}

	@Test
	void whenFindAllStudentDtos_thenReturnStudentDtos() {
		var dtoList = studentService.findAllStudentDtos();
		var csList = (List<CollegeStudent>) studentDao.findAll();

		assertEquals(dtoList.size(), csList.size());

		for (var dto : dtoList) {
			var cs = csList.iterator().next();
			assertEquals(dto.id(), cs.getId());
			assertEquals(dto.firstname(), cs.getFirstname());
			assertEquals(dto.lastname(), cs.getLastname());
			assertEquals(dto.emailAddress(), cs.getEmailAddress());
		}
	}
}