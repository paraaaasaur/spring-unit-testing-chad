# Spring Boot Support for Unit Testing

## What do we need for Spring Boot unit testing?

1. Access to Spring Application Context
2. Support for Spring dependency injection
3. Retrieve data from *application.properties*
4. Mock object support for web, data, REST APIs...

## Unit Testing Support for Spring Boot

Spring Boot provides rich testing support:

- **`@SpringBootTest`**
    - Loads the application context
    - Supports Spring dependency injection
    - Provides access to data in *application.properties*
- Implicit reference by mirror package structure & explicit reference when specified

---

## Setup

---

### Maven Dependency

- `spring-boot-starter-test`: Includes a transitive dependency on JUnit 5
    - Use `mvn dependency:tree` to examine dependency tree

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    ```
  
### Using ApplicationContext

```java
	@Autowired
	private ApplicationContext context;

    // ...

    CollegeStudent studentTwo = context.getBean(CollegeStudent.class);
```