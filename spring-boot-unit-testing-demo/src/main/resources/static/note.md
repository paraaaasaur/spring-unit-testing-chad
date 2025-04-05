# Mocking with Mockito

```text
+----------+              +---------+           +-----+        +----+
| Main App |  <-------->  | Service |  <--X-->  | DAO |  <-->  | DB |
+----------+              +---------+           +-----+        +----+
                                |
                                v
                        +-----------------+
                        | DAO Test Double |
                        +-----------------+
                        |
    -------------------------------------
    |                                   |
    "Set expectations"             "When method ABC is called, then return XYZ"
    with mock responses

*  Real-world analogy: "Theater - stand-in actor during rehearsals"
```

## Development Process

1. ✅ Create Mock for DAO
2. ✅ Inject Mock into Service
3. Set up expectations
4. Call method under tests & assert results
5. Verify method calls

## Mockito In Use

* `@Mock` for the mock objects & `@InjectMocks` for SUT depending on them.
* `when(method x).thenReturn(y);`
  - When method x (from a `@Mock` object!) is called, its output is set to y.
* Params for DOCs & SUTs have to match, even if they are just dummy values.
* `verify(mockDOC, times(2)).targetMethod(sameParam)`: 
  - Checks method being run exactly 2 times

## Mockito for Spring Boot

* Replace `@Mock` + `@InjectMocks`   
  with `@MockitoBean` + `@Autowired`