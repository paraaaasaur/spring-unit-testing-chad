package com.herbivore.test;

import com.herbivore.component.MvcTestingExampleApplication;
import com.herbivore.component.dao.ApplicationDao;
import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import com.herbivore.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = MvcTestingExampleApplication.class)
class MockAnnotationTest {

	@Autowired
	ApplicationContext context;

	@Autowired
	CollegeStudent studentOne;

	@Autowired
	StudentGrades grades;

	@Mock
	private ApplicationDao dao;

	@InjectMocks
	private ApplicationService service;

	@BeforeEach
	void beforeEach() {
		studentOne.setFirstname("John");
		studentOne.setLastname("Doe");
		studentOne.setEmailAddress("john@doe.com");
		studentOne.setStudentGrades(grades);
	}

	@DisplayName("When & Verify")
	@Test
	void assertEqualsTestAddGrades() {
		Supplier<List<Double>> supplier = () -> studentOne.getStudentGrades().getMathGradeResults();
		// Simply put: "When the x method is called then return y".
		when(dao.addGradeResultsForSingleClass(supplier.get()))
				.thenReturn(100.0);

		assertEquals(
				100.0,
				service.addGradeResultsForSingleClass(supplier.get())
		);

		verify(dao).addGradeResultsForSingleClass(supplier.get());

//		verify(dao, times(3))
//		verify(dao, times(0))
		verify(dao, times(1))
				.addGradeResultsForSingleClass(supplier.get());
	}
}