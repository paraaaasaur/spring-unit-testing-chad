package com.herbivore.springmvc.exception;

import org.springframework.http.HttpStatus;

public class GradeNotFoundException extends ApiException {

	public GradeNotFoundException(String message) {
		super(message,  HttpStatus.NOT_FOUND);
	}
}