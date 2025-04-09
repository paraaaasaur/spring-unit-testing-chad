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

	@PostMapping("/") // comment to break assertViewName
	public String createStudent(
			@ModelAttribute("whatever") CollegeStudent student,
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

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model model) {
		return "studentInformation";
	}
}