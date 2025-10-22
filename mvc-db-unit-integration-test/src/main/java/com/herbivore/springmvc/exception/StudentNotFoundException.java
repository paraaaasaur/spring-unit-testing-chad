package com.herbivore.springmvc.exception;

import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends ApiException {

	public StudentNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}