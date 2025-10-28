# Testing Spring Boot REST API

## Verifying HTTP Response

In the previous chapters, We have done certain verifying on HTTP response using `mockMvc`, like
- `status().isOk();`
- `view().name("/home");`
- `content().contentType(APPLICATION_JSON);`

But for RESTful API, we might want to also test:

- JSON body
    - Helper: JsonPath API (already bundled in `spring-boot-starter-test`)

---

# JsonPath

- Opensource project on [GitHub](https://github.com/json-path/JsonPath)

### Syntax & Examples
JsonPaths:
- `$`: The root element to query. Starts all path expressions
- `$.id`: Access the id element of the JSON element
- `$.firstname`: Access the firstname element of the JSON element

### Method (Example)
- `#jsonPath(”$”, hasSize(2));`
- …