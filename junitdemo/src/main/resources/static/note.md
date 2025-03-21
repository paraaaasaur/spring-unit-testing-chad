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

### Equality & Nullity
  - assertEquals()
  - assertNotEquals()
  - assertNull()
  - assertNotNull()

### Identity (Object Reference)
  - assertSame(), assertNotSame

### Boolean
  - assertTrue(), assertFalse()

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