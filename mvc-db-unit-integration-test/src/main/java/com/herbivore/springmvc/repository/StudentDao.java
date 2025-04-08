package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.CollegeStudent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {
	CollegeStudent findByEmailAddress(String emailAddress);

	@Query("SELECT u FROM CollegeStudent u WHERE u.emailAddress LIKE ?1")
	Set<CollegeStudent> findByEmailAddressLike(String emailAddress);
}
