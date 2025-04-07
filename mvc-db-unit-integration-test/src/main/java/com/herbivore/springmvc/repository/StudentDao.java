package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {
	CollegeStudent findByEmailAddress(String emailAddress);
}
