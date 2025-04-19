# Inject SQLs from Properties Files

## Overview

- Get SQLs out of inline `@BeforeEach` `@AfterEach`..., and inject them from **properties files**
    - `application.properties` + `@Value` — fine with a small project but not ideal when scaled & cramming everything here is a MEH

## Goals

* Finish studentInformation functionality
  - View resolution for both happy paths and error
