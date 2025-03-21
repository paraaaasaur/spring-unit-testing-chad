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

	@Test
	@DisplayName("Equals and Not Equals")
	void testEqualsAndNotEquals() {
		// 2. Execute
		int actualSum1 = demoUtils.add(2, 4);
		int actualSum2 = demoUtils.add(1, 9);

		// 3. Assertion
		assertEquals(6, actualSum1, "2 + 4 should be 6");
		assertNotEquals(6, actualSum2, "1 + 9 shouldn't be 6");
	}

	@Test
	@DisplayName("Null and Not Null")
	void testNullAndNotNull() {
		// 2. Execute
		Object checkValue1 = demoUtils.checkNull(LocalDateTime.now());
		Object checkValue2 = demoUtils.checkNull(null);

		// 3. Assert
		assertNotNull(checkValue1, "object should not be null");
		assertNull(checkValue2, "object should be null");
	}

	@Test
	@DisplayName("Same and Not Same")
	void testSameOrNotSame() {
		// 2. Execution
		String academy = demoUtils.getAcademy();
		String academyDuplicate = demoUtils.getAcademyDuplicate();
		String str = new String("free2learn Academy");

		// 3. Assertion
		assertSame(academy, academyDuplicate, "Objects should refer to the same objects");
		assertNotSame(str, academy, "Objects should refer to different objects");
	}

	@Test
	@DisplayName("True and False")
	void testTrueFalse() {
		// 1.5. Set up
		int n1 = 17;
		int n2 = 13;

		// 2. Execution
		Boolean greater = demoUtils.isGreater(n1, n2);

		// 3. Assertion
		assertTrue(greater, "17 > 13 should be true");
		assertFalse(!greater, "17 > 13 should be false");
	}

	private static class Archive {
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
	}
}
