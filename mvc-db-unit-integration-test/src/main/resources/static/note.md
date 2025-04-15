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
2. POST(create student): Application/JSON(content-type) + model ⇒ return to view index.html
   - Test
       ```java
       @DisplayName("TDD for POST-Create Student Endpoint")
       @Test
       void createStudentHttpRequest() throws Exception {
           // 1. Pathway check: from entry(request) to exit("index.html")
           MvcResult mvcResult = mockMvc.perform(post("/")
                           .contentType(MediaType.APPLICATION_JSON)
                           .param("firstname", "John")
                           .param("lastname", "Doe")
                           .param("emailAddress", "jd@gmail.com"))
                   .andExpect(status().isOk())
                   .andReturn();
    
           ModelAndView mav = mvcResult.getModelAndView();
           ModelAndViewAssert.assertViewName(mav, "index");
    
           // 2. Functionality check: create student
           CollegeStudent dbStudent = studentDao
                   .findByEmailAddress(requestMock.getParameter("emailAddress"));
           assertNotNull(dbStudent, "Student should've been created");
       }
       ```
   - Tested controller method
       ```java
       @PostMapping("/") // comment to break assertViewName
       public String createStudent(
               @ModelAttribute("whatever-when-receive-only") CollegeStudent student,
       //			@ModelAttribute("student") CollegeStudent student,
               Model model
       ) {
           // comment to break assertNotNull
           studentAndGradeService.createStudent(
                   student.getFirstname(),
                   student.getLastname(),
                   student.getEmailAddress()
           );
    
           return "index";
       }
       ```
---

## Update the UI

* Data binding:
  - Request/x-www-form-urlencoded: name-value pairs  
  - To-Controller: DTO parameter
    - Doesn't even require annotations like `@ModelAttribute`, `@RequestBody`
    - Doesn't require explicitly adding or accessing a `Model`