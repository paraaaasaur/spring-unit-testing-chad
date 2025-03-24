package com.herbivore.tdd;

public class FizzBuzz {

	// Refactored version
	public static String compute(int n) {
		StringBuilder result = new StringBuilder();

		if (n % 3 == 0 && n % 5 == 0) {
			result.append("FizzBuzz");
		}
		else if (n % 3 == 0) {
			result.append("Fizz");
		}
		else if (n % 5 == 0) {
			result.append("Buzz");
		}
		else {
			result.append(n);
		}

		return result.toString();
	}

/*
	public static String compute(int n) {

		if (n % 3 == 0 && n % 5 == 0) {
			return "FizzBuzz";
		}

		else if (n % 3 == 0) {
			return "Fizz";
		}

		else if (n % 5 == 0) {
			return "Buzz";
		}

		else {
			return String.valueOf(n);
		}
	}
*/
}
