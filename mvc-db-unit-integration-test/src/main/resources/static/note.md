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


## `MockMvc` Workflow Scenarios
---
(Too many trivial helper classes here... use `import static`)
1. GET request + status code 200(isOk()) ⇒ return to view index.html
    ```java
    @Test
    public void getStudentsHttpRequest () throws Exception {
        // 1. GET request | expects HTTP 200 OK
        MvcResult mvcResult = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();
    
        // 2. View resolution | verifies: the view returned == "index.html"
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");
    }
    ```
---