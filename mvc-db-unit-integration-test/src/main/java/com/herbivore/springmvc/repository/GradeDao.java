package com.herbivore.springmvc.repository;

import com.herbivore.springmvc.model.Grade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

// a mere interface for real repos to implements; not a repository by itself
@NoRepositoryBean
public interface GradeDao<T extends Grade> extends CrudRepository<T, Integer> {}