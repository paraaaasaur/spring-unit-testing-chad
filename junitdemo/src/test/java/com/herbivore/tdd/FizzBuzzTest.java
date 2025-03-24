package com.herbivore.tdd;

import org.junit.jupiter.api.*;

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
}
