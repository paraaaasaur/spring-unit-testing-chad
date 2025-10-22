# Testing Spring Boot REST API

## Rework Current Fullstack Project → RESTful API, Backend only 

### Files
- Added test sql files: _insert-grade.sql_, _override-data.sql_
- _insert-student.sql_:
  - Renamed _insert-student.sql_
  - Before: insert 4 students without id assignment
  - After: assign them as id 11~14 to match FK need of grade insertion

### Views

No relevance from here - This app is from now on a REST API
- Can be a nice practice for building RESTful frontend later using JS/TS/React

### application[-test].properties

No worries.

### Error Handling
new classes:
- Exception: `ApiException`, `GradeNotFoundException`, `StudentNotFoundException`
- ~~Custom ResponseEntity: `ApiErrorPayload`~~ -> use the standard `ProblemDetail` class (since Spring Boot 3.0/Spring 6.0)
- `@ControllerAdvice` Error Handler: `GlobalErrorHandler` (specialized controller)

### Controller
`@Controller` -> `@RestController`
- Model/Attributes omitted (check params, method body, return types)
- Renamed some endpoints to be more consistent + RESTful
- Precise annotations (e.g. `@DeleteMapping` rather than GET anywhere) for http methods

### Repository

- Created common repo `@NoRepositoryBean GradeDao<T extends Grade> extends CrudRepository<T, Integer>` for concrete repos to inherit

### StudentAndGradeServiceTest.class

- Reworked `getGradebookService`
- Added `isGradeFoundService`
The rest are minor tweaks.

### Grade (interface)

- New setter/getter for CollegeStudent needed to implement for child classes

### Models and Entities
`GradebookControllerTest.class`
- Deleted, since it's a useless DTO, and far less useful than a usual, well-established CollegeStudent JPA entity

`StudentGrades.class`
- Deleted for the same reason. Just use a well-tuned JPA entity CollegeStudent or a collection for grades.

`CollegeStudent.class`
- resolve infinite JSON recursion using `@JsonManagedReference` (see also `@JsonBackReference` in child entity classes)
- remake associate/dissociate methods to be generic
- 1-M collections: List(Bag) → Set to 
  - get more aligned to entity semantics
  - get immune to Cartesian product issue from JOIN FETCHing multiple collections, which Bags cannot resolve

Grade classes
- resolve infinite JSON recursion using `@JsonBackReference` (see also `@JsonManagedReference` in parent entity class)

`enum Grade.Subject.class`
- Renamed from `Grade.Type`
- new `of(Class<? extends Grade> clazz)` method to get enum constants from Grade class

### StringToGradeSubjectConverter implements Converter<String, Grade.Subject>

Now request String → Subject enum conversion is case-insensitive 

### StudentAndGradeService

- `getGradebook` -> `findAllStudentWithGrades`
  - Before: returns `Iterable<CollegeStudent>`
  - After: returns `List<CollegeStudent>` + initialize lazy fields
- `studentInformation` -> `findStudentWithGrade`
  - Before: 
  - After: now initialize lazy fields
- Removed `isStudentFound`, `isGradeFound`... Just use `repo.existsById`

- Rework methods styles:
  - Throws meaningful exceptions
  - Return types now lean more to repository ones
  - Less DRY + private helper methods