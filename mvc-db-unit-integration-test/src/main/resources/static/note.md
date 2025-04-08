# MVC+DB Web App & Unit/Integration Test

## H2 Database

* Embedded, in-memory db (like H2) is good to go by just adding to dependency.
  - No need for manual connection setup 
  - Further customization is feasible as well

### Sample Data

#### Set up & clean up

* Where? → `@beforeEach` and `@afterEach` methods
* How? → **`jdbcTemplate.execute()`** + SQL statements
  - A helper class in Spring framework, to provide handy JDBC-level operations