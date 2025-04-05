package com.herbivore.test;

import com.herbivore.component.MvcTestingExampleApplication;
import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {

	@Autowired
	ApplicationContext context;

	@Autowired
	CollegeStudent studentOne;

	@Autowired
	StudentGrades grades;

	@BeforeEach
	void beforeEach() {
		studentOne.setFirstname("Garrosh");
		studentOne.setLastname("Hellscream");
		studentOne.setEmailAddress("for-the-horde@blizzard.com");
		studentOne.setStudentGrades(grades);
	}
}
