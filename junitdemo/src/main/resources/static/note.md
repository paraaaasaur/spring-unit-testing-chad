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

* Static methods from `Assertions.class`
  - assertEquals()
  - assertNotEquals()
  - assertNull()
  - assertNotNull()

