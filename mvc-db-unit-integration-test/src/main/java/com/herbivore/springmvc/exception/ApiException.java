package com.herbivore.springmvc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class ApiException extends RuntimeException /*implements ErrorResponse*/ {
//	private final ErrorCode errorCode;
	private final HttpStatus status;


	public ApiException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public ApiException(String message) {
		this(message, BAD_REQUEST);
	}
}