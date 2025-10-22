package com.herbivore.springmvc.controller;

import com.herbivore.springmvc.exception.ApiException;
import com.herbivore.springmvc.exception.GradeNotFoundException;
import com.herbivore.springmvc.exception.StudentNotFoundException;
import com.herbivore.springmvc.model.Grade;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/* modifier "public": mainly for convention and style
technically, you can make it package-private, but making it public has some benefits:
(1) consistency with controller methods
(2) readability and discoverability: signals readers that
“This is a key entry point of the web layer,” not just an internal helper.
(3) easier to test or reuse
*/
@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleStudentNotFound(StudentNotFoundException ex) {
		var body = toProblemDetail(ex);

		return ResponseEntity
				.status(body.getStatus())
				.body(body);
	}

	@ExceptionHandler(GradeNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleGradeNotFound(GradeNotFoundException ex) {
		var body = toProblemDetail(ex);

		return ResponseEntity
				.status(body.getStatus())
				.body(body);
	}

	@ExceptionHandler({ApiException.class})
	public ResponseEntity<ProblemDetail> handleGenericApiException(ApiException ex) {
		var body = toProblemDetail(ex);

		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleException(Exception ex) {
		var body = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
		body.setDetail("Unexpected server error");
		body.setProperty("timestamp", System.currentTimeMillis());

		return ResponseEntity
				.status(body.getStatus())
				.body(body);
	}

	// override just to customize more personal 'detail' message
	// in case of Subject enum conversion failure on bad request
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ResponseEntity<Object> entity = super.handleTypeMismatch(ex, headers, status, request);
		if (entity == null) {
			return null;
		}

		Object body = entity.getBody();
		if (body instanceof ProblemDetail problemDetail) {
			if (ex.getRequiredType() == Grade.Subject.class) {
				System.err.println("Chikuwa daimyoujin #2025-10-30 01:24");
				String newDetail = String.format("Invalid %s '%s'",
						ex.getPropertyName(),
						ex.getValue()
			);
				problemDetail.setDetail(newDetail);
			}
		}
//		return entity;
		// defensive: make a new one in case of immutability
		return ResponseEntity.status(status).headers(headers).body(body);
	}


	// static methods
	/**
	 * Return: A {@code ProblemDetail} instance defaulted
	 * with status + detail + timestamp
	 * from the given {@code ApiException}
	 **/
	protected static ProblemDetail toProblemDetail(@NotNull ApiException ex) {
		ProblemDetail body = ProblemDetail.forStatus(ex.getStatus());
		body.setDetail(ex.getMessage());
		body.setTitle("Api Error");
		// Add custom properties if needed
		body.setProperty("timestamp", System.currentTimeMillis());
		return body;
	}
}