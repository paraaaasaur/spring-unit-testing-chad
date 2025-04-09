package com.herbivore.springmvc.controller;

import com.herbivore.springmvc.model.Gradebook;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GradebookController {
	private final Gradebook gradebook;
	private final StudentAndGradeService studentAndGradeService;

	@Autowired
	public GradebookController(Gradebook gradebook, StudentAndGradeService studentAndGradeService) {
		this.gradebook = gradebook;
		this.studentAndGradeService = studentAndGradeService;
	}


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model model) {
		var collegeStudents = studentAndGradeService.getGradebook();
		model.addAttribute("students", collegeStudents);
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model model) {
		return "studentInformation";
	}
}
