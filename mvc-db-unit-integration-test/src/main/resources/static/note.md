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

### H2 Properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.initialization-mode=always
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Using Separate SQL Files

1. make a .sql script under test resources
2. `@Sql("/insert-data.sql")` on the test method

## You know...

* `@Query("sql-like statement")` can be used on `CrudRepository` for custom queries 