package com.herbivore;

import com.herbivore.tdd.FizzBuzz;

import java.util.stream.IntStream;

public class Main {
	public static void main(String[] args) {
		IntStream.rangeClosed(1, 100)
				.forEach(i -> {
					if (i % 10 == 0) System.out.println();
					System.out.printf("%-8s ", FizzBuzz.compute(i));
				});
	}
}
