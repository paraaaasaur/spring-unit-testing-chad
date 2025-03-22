# JUnit

## Project Setup

* Dependency: 
  * artifactId: junit-jupiter
  * scope: test

## JUnit Basics

* Test class doesn't have to be public
* Development process:
  1. Add Dependency
  2. Create test package
     - Mirror to that under main/, except it's under test/ here
  3. Create unit test
     - Set up → Execute → Assert
  4. Run unit test

## Basic Assertions
Using static methods from `Assertions.class`

* Equality: assertEquals, assertNotEquals
* Nullity: assertNull, assertNotNull

* Identity (Object Reference): assertSame, assertNotSame
* Boolean: assertTrue, assertFalse

* Array Deeply Equal: check for equality of nested instances (not reference check)
  - assertArrayEquals
* Iterable Deeply Equal: Collection, Map, DirectoryStream, ...
  - assertIterableEquals
* Lines Match: Quick check for List<Stream>
  - assertLinesMatch

* Throws: assertThrow
* Timeout: 
  - `assertTimeout`: test timeout 
  - `assertTimeoutPreemptively`: test timeout, but abort rest of the code execution if timeout 


## Lifecycle Methods

---

### Common Operations Around Test Methods

* `@BeforeEach`: run before each test
    - create objects, set up test data...
* `@AfterEach`: run after each test
    - release resources, clean up test data...

### One-Time Method

* `@BeforeAll`: Setup before all tests
    - Database connection, connect to remote server...
* `@AfterAll`: Clean up after all tests
    - release db connection, disconnect from remote server...


## Create Display Method

* To be more descriptive than the raw method name
* To include spaces/emojis/special characters 
  - e.g. Test for Equality to support JIRA #123
* Easier for non-techies members to understand

* `@DisplayName`
* `@DisplayNameGeneration`: Auto generates display name using: 
  - Simple, DisplayUnderscores, IndicativeSentences
  - JUnit doesn't provide a built-in generator for camel case (DIY if you wish)


## Order in Unit Test
* [Docs](https://junit.org/junit5/docs/current/user-guide/#writing-tests-test-execution-order) By default, test classes and methods will be ordered using an algorithm that is deterministic but intentionally nonobvious. This ensures that subsequent runs of a test suite execute test classes and test methods in the same order, thereby allowing for repeatable builds.

* In general, order SHOULD NOT be a factor in unit tests
  - AKA There should be NO dependency between unit tests
  - AKA "Passing or not" is regardless of unit testing order

* Reasons we might want ordering:
  - Sort tests to be alphabetical for reporting purposes
  - Group tests based on functionality, requirements...
  - Share report with project manager, QA teams...

* `@TestMethodOrder` over test class
  * Ordering Types:
    1. Display name (alphabetically): `MethodOrderer.DisplayName`
    2. Method name (alphabetically): `MethodOrderer.MethodName`
    3. Randomly: `MethodOrderer.Random`
    4. Custom: `MethodOrderer.OrderAnnotation` + `@Order`

## Code Coverage

* Code coverage means "how many lines/methods are called by your tests"
* In general, the higher the coverage the better
  - However, 100% is not always attainable
  - On most teams, 70%~80% is acceptable

<aside>
‼️

WARNING: Code coverage is only a metric, and can easily be tricked with bad tests.  
See it as simply one data point in our development process
</aside>

### Intellij Support for Test Results/Code Coverage

* Can generate coverage reports in the IDE
  * "Run Code With Coverage" 
     * Covered methods are in green gutter, while not covered methods are in red. 
* Can generate HTML/XML output to view test results/code coverage in web browser
  - "Coverage" tab > "Generate Coverage Report"
  - "Cover" tab > "Export Test Results..."

### Maven Support for Code Coverage

* Maven offers the same support agnostic of your IDE