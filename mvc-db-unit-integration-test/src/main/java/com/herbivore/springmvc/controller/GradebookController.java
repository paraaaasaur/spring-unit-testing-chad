package com.herbivore.springmvc.controller;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.Grade;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class GradebookController {
	private final StudentAndGradeService studentAndGradeService;

	@Autowired
	public GradebookController(StudentAndGradeService studentAndGradeService) {
		this.studentAndGradeService = studentAndGradeService;
	}


	@GetMapping("/")
	public List<CollegeStudent> getStudents() {
		return studentAndGradeService.findAllStudentsWithGrades();
	}

	@GetMapping("/studentInformation/{id}")
	public CollegeStudent studentInformation(@PathVariable int id) {
		return studentAndGradeService.findStudentWithGrades(id);
	}

	@PostMapping("/student")
	@ResponseStatus(HttpStatus.CREATED)
	public List<CollegeStudent> createStudent(@RequestBody CollegeStudent newStudent) {
		studentAndGradeService.createStudent(
				newStudent.getFirstname(),
				newStudent.getLastname(),
				newStudent.getEmailAddress()
		);
		return studentAndGradeService.findAllStudentsWithGrades();
	}

	@DeleteMapping("/student/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public List<CollegeStudent> deleteStudent(@PathVariable int id) {
		studentAndGradeService.deleteStudent(id);
		return studentAndGradeService.findAllStudentsWithGrades();
	}

	@PostMapping("/grades")
	@ResponseStatus(HttpStatus.CREATED)
	public CollegeStudent createGrades(
			@RequestParam Grade.Subject subject,
			@RequestParam double grade,
			@RequestParam int studentId
	) {
		studentAndGradeService.createGrade(grade, studentId, subject);
		return studentAndGradeService.findStudentWithGrades(studentId);
	}

	@DeleteMapping("/grades/{id}/{subject}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public CollegeStudent deleteGrade(@PathVariable int id, @PathVariable Grade.Subject subject) {
		int studentId = studentAndGradeService.deleteGrade(id, subject);
		return studentAndGradeService.findStudentWithGrades(studentId);
	}
}