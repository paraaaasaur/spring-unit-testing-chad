package com.herbivore.test;

import com.herbivore.component.MvcTestingExampleApplication;
import com.herbivore.component.dao.ApplicationDao;
import com.herbivore.component.models.CollegeStudent;
import com.herbivore.component.models.StudentGrades;
import com.herbivore.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.function.Supplier;

import static com.herbivore.test.Dummies.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = MvcTestingExampleApplication.class)
class MockAnnotationTest {

	@Autowired
	ApplicationContext context;

	@Autowired
	CollegeStudent studentOne;

	@Autowired
	StudentGrades grades;

//	@Mock
	@MockitoBean
	private ApplicationDao dao;

//	@InjectMocks
	@Autowired
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

	@DisplayName("Find Gpa")
	@Test
	void assertEqualsTestFindGpa() {
		when(dao.findGradePointAverage(dummyDoubleList))
				.thenReturn(88.31);

		assertEquals(88.31, service.findGradePointAverage(dummyDoubleList));

		verify(dao, times(1))
//				.findGradePointAverage(List.of());
				.findGradePointAverage(dummyDoubleList);
	}

	@DisplayName("Not Null")
	@Test
	void testAssertNotNull() {
		when(dao.checkNull(grades.getMathGradeResults()))
				.thenReturn(dummyObj);

		assertNotNull(
				service.checkNull(studentOne.getStudentGrades().getMathGradeResults()),
				"Object should not be null"
		);
	}

	@DisplayName("Multiple Stubbing")
	@Test
	void stubbingConsecutiveCalls() {
		Executable daoExe = () -> dao.checkNull(dummyObj);
		Executable serviceExe = () -> service.checkNull(dummyObj);
		final String message = "No Exception thrown second time";


		when(dao.checkNull(dummyObj))
				.thenThrow(new RuntimeException("Aha!"))
				.thenReturn(message);


		assertThrows(Exception.class, serviceExe);
		System.out.println("> 1");

		assertThrows(Exception.class, serviceExe);
//		assertEquals(message, service.checkNull(dummyObj));
		System.out.println("> 2");

		verify(dao, times(2)).checkNull(dummyObj);
	}

	@DisplayName("Throw Exceptions")
	@Test
	void throwRuntimeException() {
		Executable daoExe = () -> dao.checkNull(dummyObj);
		Executable serviceExe = () -> service.checkNull(dummyObj);


		// # Make a method x throw exception y
		// Method 1: General version - Not void-proof
//		when(dao.checkNull(dummyObj))
//				.thenThrow(new UnsupportedOperationException("Exception #1"))
//				.thenThrow(new IllegalArgumentException("Exception #2"));

		// Method 2: Void-proof version
		doThrow(new UnsupportedOperationException("Exception #1"))
				.doThrow(new IllegalArgumentException("Exception #2"))
				.when(dao).checkNull(dummyObj);


		assertThrows(UnsupportedOperationException.class, serviceExe);
		System.out.println("> 1");

		assertThrows(IllegalArgumentException.class, serviceExe);
		System.out.println("> 2");

		assertThrows(UnsupportedOperationException.class, serviceExe);
//		assertThrows(IllegalArgumentException.class, serviceExe);
		System.out.println("> 3");
	}
}