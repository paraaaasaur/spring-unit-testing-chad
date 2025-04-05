package com.herbivore.test;

import com.herbivore.component.MvcTestingExampleApplication;
import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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

		ReflectionTestUtils.setField(studentOne, "id", 1);
		ReflectionTestUtils.setField(studentOne, "studentGrades",
				new StudentGrades(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
	}

	@Test
	void getPrivateField() {
		final int expected = 9999999;
//		final int expected = 1;
		final Integer actual = (Integer)
				ReflectionTestUtils.getField(studentOne, "id");

		assertEquals(expected, actual);
	}

	@Test
	void invokePrivateMethod() {
		final String expected = "Garrosh 9999999";
//		final String expected = "Garrosh 1";
		final String actual = ReflectionTestUtils.invokeMethod(
				studentOne, "getFirstNameAndId");

		assertEquals(expected, actual, "Fail private method not call");
	}
}
