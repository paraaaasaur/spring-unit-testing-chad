package com.herbivore.component;

import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Set;

@SpringBootTest
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

	@BeforeEach
	void beforeEach() {
		count++;
		System.out.printf(
				"Testing: %s which is %s Version: %s. Execution of test method: %d\n",
				appInfo, appDescription, appVersion, count);
		student.setFirstname("John");
		student.setLastname("Doe");
		student.setEmailAddress("john@doe.com");
		studentGrades.setMathGradeResults(new ArrayList<>(Set.of(100.0, 77.5, 88.3, 55.4)));
		student.setStudentGrades(studentGrades);
	}

	@Test
	void basicTest() {
		assert true;
	}
}
