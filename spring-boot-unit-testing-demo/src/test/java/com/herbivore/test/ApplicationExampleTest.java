package com.herbivore.test;

import com.herbivore.component.MvcTestingExampleApplication;
import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class ApplicationExampleTest {

	private static int count = 0;

	@Value("${info.app.name}")
	private String appInfo;

	@Value("${info.app.description}")
	private String appDescription;

	@Value("${info.app.version}")
	private String appVersion;

	@Value("${info.school.name}")
	private String schoolName;

	@Autowired
	private CollegeStudent student;

	@Autowired
	private StudentGrades studentGrades;

	// used to retrieve Spring bean
	@Autowired
	private ApplicationContext context;

	@BeforeEach
	void beforeEach() {
		count++;
		System.out.printf(
				"Testing: %s which is %s Version: %s.\n" +
				"Execution of test method: %d\n",
				appInfo, appDescription, appVersion, count);
		student.setFirstname("John");
		student.setLastname("Doe");
		student.setEmailAddress("john@doe.com");
		studentGrades.setMathGradeResults(new ArrayList<>(Set.of(100.0, 77.5, 88.3, 55.4)));
		student.setStudentGrades(studentGrades);
	}

	@DisplayName("Add grade results for student grades")
	@Test
	void addGradeResultsForStudentGrades() {
		double expected = 321.2;
		double actual = studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults());

		assertEquals(expected, actual, "Grades should match");
	}

	@DisplayName("Add grade results for student grades not equal")
	@Test
	void addGradeResultsForStudentGradesNotEquals() {
		double expected = Double.NaN;
		double actual = studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults());

		assertNotEquals(expected, actual, "Grades should NOT match");
	}

	@DisplayName("Is grade greater")
	@Test
	void isGradeGreaterStudentGrades() {
		assertTrue(studentGrades.isGradeGreater(90, 75),
				"failure - should be true");
	}

	@DisplayName("Is grade greater false")
	@Test
	void isGradeGreaterStudentGradesAssertFalse() {
		assertFalse(studentGrades.isGradeGreater(89, 92),
				"failure - should be false");
	}

	@DisplayName("Check Null for student grades")
	@Test
	void checkNullForStudentGrades() {
		assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
				"object should not be null");
	}

	@DisplayName("Create student without grade init")
	@Test
	void createStudentWithoutGradeInit() {
		CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);

		studentTwo.setFirstname("Ryoma");
		studentTwo.setLastname("Sakamoto");
		studentTwo.setEmailAddress("skmt@gmail.com");

		assertNotNull(studentTwo.getFirstname());
		assertNotNull(studentTwo.getLastname());
		assertNotNull(studentTwo.getEmailAddress());
		assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));
	}

	@DisplayName("Verify student bean is prototype")
	@Test
	void verifyStudentBeanIsPrototype() {
		CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);

		// Singleton: same object; prototype: new object
		assertNotSame(student, studentTwo);
	}

	@DisplayName("Find grade points average")
	@Test
	void findGradePointsAverage() {
		Executable exe1 = () -> assertEquals(321.2, studentGrades.addGradeResultsForSingleClass(
				student.getStudentGrades().getMathGradeResults()));
		Executable exe2 = () -> assertEquals(80.3, studentGrades.findGradePointAverage(
				student.getStudentGrades().getMathGradeResults()));

		assertAll(exe1, exe2);
	}
}
