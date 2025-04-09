# Controller Testing

# Development Process

---

1. Add annotation `@AutoConfigureMockMvc`
2. Inject `MockMvc`
3. Perform web requests
4. Define expectations
5. Assert results

## Boilerplate Setup for MVC Testing

- `MockMvc`: Main entry point for server-side Spring MVC test support
- `@AutoConfigureMockMvc`: In pair with ↑ for necessary configuration

```java
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest(classes = GradebookController.class)
public class GradebookControllerTest {
	private final JdbcTemplate jdbcTemplate;
	private final MockMvc mockMvc;
	@Mock
	private StudentAndGradeService studentAndGradeService;

	public GradebookControllerTest(JdbcTemplate jdbcTemplate, MockMvc mockMvc) {
		this.jdbcTemplate = jdbcTemplate;
		this.mockMvc = mockMvc;
	}

	@BeforeEach
	void setup() {
		// ...
	}

	@AfterEach
	void cleanUpAfterTransaction() {
		// ...
	}
	
	// test methods from here
}
```

---