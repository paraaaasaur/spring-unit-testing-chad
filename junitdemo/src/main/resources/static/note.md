# CH3 Test-Driven Development

*  
		       Test(start) 
		       ↗︎      ↘︎
		      ↗︎        ↘︎
		     ↗︎          ↘︎
		 Refactor ←←←   Code
* Write tests -> Add logic to pass the tests
* After all tests are cleared, feel free to refactor as long as it still passes tests

## Parameterized Tests

---

- **TL;DR**: Feed multiple sets of (input, expected) to the test
- Can be done **manually** with inline code + for-loop — but JUnit provides tools for the work

    ```java
    @DisplayName("Loop over Array")
    @Test
    @Order(5)
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
    ```


## JUnit Support for Parameterized Tests

- `@ParameterizedTest` replaces `@Test`
- Sources of Values: `@ValueSource` **`@CsvSource`** **`@CsvFileSource`** `@EnumSource` `@MethodSource`
	- `@CsvSource`

	    ```
        
        ```

	- `@CsvFileSource`

	    ```
        *# resources/data/small-test-data.csv*
        0,FizzBuzz
        1,1
        2,2
        3,Fizz
        4,4
        5,Buzz
        6,Fizz
        7,7
        8,8
        9,Fizz
        10,Buzz
        11,11
        12,Fizz
        13,13
        14,14
        15,FizzBuzz
        ```

	    ```java
        // FizzBuzzTest.java
        @DisplayName("Testing with Small Data File")
        @ParameterizedTest(name = "value={0}, expected={1}")
        @CsvFileSource(resources = "/data/small-test-data.csv")
        @Order(5)
        void testForLoopOverArray(int value, String expected) {
            assertEquals(expected, FizzBuzz.compute(value));
        }
        ```