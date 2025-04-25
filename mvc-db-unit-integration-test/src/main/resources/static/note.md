# Inject SQLs from Properties Files

## Overview

- Get SQLs out of inline `@BeforeEach` `@AfterEach`..., and inject them from **properties files**
    - `application.properties` + `@Value` — fine with a small project but not ideal when scaled & cramming everything here is a MEH

## Goals

* Finish studentInformation functionality
  - View resolution for both happy paths and error

## application-test.properties

* We want a dedicated one for testing, rather than "borrowing" the main one

1. Copy _application.properties_ in the main app
2. Paste it under src/test/resources
3. Rename it to _application-test.properties_
4. Modify the source file of `@TestPropertySource` in all test classes  
   (Rerun the full test suite to make sure we didn't break anything!)
   - Before: `@TestPropertySource("/application.properties")`
   - After: `@TestPropertySource("/application-test.properties")`
5. Replace H2 in-memory configs with MySQL