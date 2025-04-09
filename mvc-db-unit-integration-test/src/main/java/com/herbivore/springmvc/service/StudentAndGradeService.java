package com.herbivore.springmvc.service;

import com.herbivore.springmvc.model.CollegeStudent;
import com.herbivore.springmvc.repository.StudentDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {
	private final StudentDao studentDao;


	public StudentAndGradeService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}


	public void createStudent(String firstname, String lastname, String emailAddress) {
		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
		student.setId(0);
		studentDao.save(student);
	}

	public boolean isStudentFound(int id) {
		return studentDao.findById(id).isPresent();
	}

	public void deleteStudent(int id) {
		if (isStudentFound(id))
			studentDao.deleteById(id);
	}

	public Iterable<CollegeStudent> getGradebook() {
		return studentDao.findAll();
	}
}
