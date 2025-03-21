package com.herbivore;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// No need to use public visibility
class DemoUtilsTest {

	DemoUtils demoUtils;

	@BeforeEach
	void setUpBeforeEach() {
		// 1. Set up
		System.out.println("@BeforeEach: Something need doing?");
		demoUtils = new DemoUtils();
	}

	@AfterEach
	void tearDownAfterEach() {
		System.out.println("@AfterEach: Job's done!");
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("@BeforeAll: Hail, friend!");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("@AfterAll: We shall meet again.");
	}

	@Test
	void testEqualsAndNotEquals() {

		System.out.println("Running: testEqualsAndNotEquals");

		// 2. Execute
		int actualSum1 = demoUtils.add(2, 4);
		int actualSum2 = demoUtils.add(1, 9);

		// 3. Assertion
		assertEquals(6, actualSum1, "2 + 4 should be 6");
		assertNotEquals(6, actualSum2, "1 + 9 shouldn't be 6");
	}

	@Test
	void testNullAndNotNull() {

		System.out.println("Running: testNullAndNotNull");

		// 2. Execute
		Object checkValue1 = demoUtils.checkNull(LocalDateTime.now());
		Object checkValue2 = demoUtils.checkNull(null);

		// 3. Assert
		assertNotNull(checkValue1, "object should not be null");
		assertNull(checkValue2, "object should be null");
	}

}
