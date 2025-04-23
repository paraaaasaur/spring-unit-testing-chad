package com.herbivore.springmvc.controller;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.model.Grade;
import com.herbivore.springmvc.repository.StudentDao;
import com.herbivore.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class GradebookController {
	private final StudentAndGradeService studentAndGradeService;
	private final StudentDao studentDao;

	@Autowired
	public GradebookController(StudentAndGradeService studentAndGradeService, StudentDao studentDao) {
		this.studentAndGradeService = studentAndGradeService;
		this.studentDao = studentDao;
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
		var dbStudentOpt = studentDao.findById(id);
		if (dbStudentOpt.isEmpty()) {
			return "error";
		}

		putGcsAndGradesToModel(id, model);


		return "studentInformation";
	}

	@PostMapping("/grades")
	public String createGrades(
			@RequestParam Grade.Type gradeType,
			@RequestParam double grade,
			@RequestParam int studentId,
			Model model
	) {
//		System.out.println(gradeType + " " + grade + " " + studentId);
		if (!studentAndGradeService.isStudentFound(studentId)) {
			return "error";
		}

		if (!studentAndGradeService.createGrade(grade, studentId, gradeType)) {
			return "error";
		}

		putGcsAndGradesToModel(studentId, model);

		return "redirect:/studentInformation/" + studentId;
	}

	@PostMapping("/grades/{id}/{gradeType}")
	public String deleteGrade(@PathVariable int id, @PathVariable Grade.Type gradeType) {
		int studentId = studentAndGradeService.deleteGrade(id, gradeType);
		boolean success = studentId > 0;

		return success?
				"redirect:/studentInformation/" + studentId :
				"error";
//				"forward:/error";
	}


	// helper methods
	private void putGcsAndGradesToModel(int studentId, Model model) {
		var gcs = studentAndGradeService.studentInformation(studentId);
		model.addAttribute("student", gcs);


		final var historyResults = gcs.getStudentGrades().getHistoryGradeResults();
		final var mathResults = gcs.getStudentGrades().getMathGradeResults();
		final var scienceResults = gcs.getStudentGrades().getScienceGradeResults();

		var historyAverage = historyResults.isEmpty()?
				"N/A" :
				Grade.avgGrades(historyResults);
		model.addAttribute("historyAverage", historyAverage);

		var mathAverage = mathResults.isEmpty()?
				"N/A" :
				Grade.avgGrades(mathResults);
		model.addAttribute("mathAverage", mathAverage);

		var scienceAverage = scienceResults.isEmpty()?
				"N/A" :
				Grade.avgGrades(scienceResults);
		model.addAttribute("scienceAverage", scienceAverage);
	}
}