package com.herbivore.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * If number is divisible by 3 → print Fizz <br>
 * If number is divisible by 5 → print Buzz <br>
 * If number is divisible by 3 and 5 → print FizzBuzz <br>
 * If number is BOT divisible by 3 or 5 → print the number <br>
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

	@DisplayName("Divisible by 3")
	@Test
	@Order(2)
	void testForDivisibleByThree() {
		String expected = "Fizz";
		String actual = FizzBuzz.compute(3);

		assertEquals(expected, actual, "Should return Fizz");
	}

	@DisplayName("Divisible by 5")
	@Test
	@Order(1)
	void testForDivisibleByFive() {
		String expected = "Buzz";
		String actual = FizzBuzz.compute(5);

		assertEquals(expected, actual, "Should return Buzz");
	}

	@DisplayName("Divisible by 3 And 5")
	@Test
	@Order(3)
	void testForDivisibleByThreeAndFive() {
		String expected = "FizzBuzz";
		String actual = FizzBuzz.compute(15);

		assertEquals(expected, actual, "Should return FizzBuzz");
	}

	@DisplayName("Not Divisible by 3 or 5")
	@Test
	@Order(4)
	void testForNotDivisibleByThreeOrFive() {
		String expected = "2";
		String actual = FizzBuzz.compute(2);

		assertEquals(expected, actual, "Should return 2");
	}

	@DisplayName("Testing with Small Data File")
	@ParameterizedTest(name = "value={0}, expected={1}")
	@CsvFileSource(resources = "/data/small-test-data.csv")
	@Order(5)
	void testForLoopOverArray(int value, String expected) {
		assertEquals(expected, FizzBuzz.compute(value));
	}

	private class Archive {
//		@DisplayName("Loop over Array")
//		@Test
//		@Order(5)
		void testForLoopOverArray() {
			Object[][] data = {
					{1, "1"},
					{2, "2"},
					{3, "Fizz"},
					{4, "4"},
					{5, "Buzz"},
					{6, "Fizz"},
					{7, "7"},
					{15, "FizzBuzz"}
			};

			for (Object[] row : data) {
				assertEquals(row[1], FizzBuzz.compute((int)row[0]));
			}
		}
	}
}
