package com.herbivore.springmvc.controller;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {
	private final StudentAndGradeService studentAndGradeService;

	@Autowired
	public GradebookController(StudentAndGradeService studentAndGradeService) {
		this.studentAndGradeService = studentAndGradeService;
	}


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model model) {
		var collegeStudents = studentAndGradeService.getGradebook();
		model.addAttribute("students", collegeStudents);
		return "index";
	}

	@PostMapping(name = "/") // comment to break assertViewName
	public String createStudent(
			CollegeStudent student,
			Model model
	) {
		// comment to break assertNotNull
		studentAndGradeService.createStudent(
				student.getFirstname(),
				student.getLastname(),
				student.getEmailAddress()
		);
		System.out.println(model);

		return "redirect:/";
	}

	@PostMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id) {
		if (!studentAndGradeService.isStudentFound(id)) {
			return "error";
		}

		studentAndGradeService.deleteStudent(id);
		return "redirect:/";
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model model) {
		return "studentInformation";
	}
}