package com.galvao.permission.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galvao.permission.exception.exeptions.HttpException;
import com.galvao.permission.exception.response.ErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.util.Objects;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity handleException(Throwable throwable) {
		log.error(throwable.getMessage(), throwable);
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
	}

	@ExceptionHandler
	public ResponseEntity handleException(HttpException throwable) {
		log.error(throwable.getMessage(), throwable);
		return ResponseEntity.status(throwable.getHttpStatus()).body(new ErrorResponse(throwable.getMessage(), null));
	}

	@ExceptionHandler
	public ResponseEntity handlerServerInputException(ServerWebInputException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(new ErrorResponse("Bad request.", e.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage(), e);
		String message = String.format("Argument '%s' should be valid '%s' but is '%s'.",
				e.getName(), Objects.requireNonNull(e.getRequiredType()).getSimpleName(), e.getValue());
		return new ResponseEntity<>(new ErrorResponse(message, e.getMessage()), BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity handleJsonProcessingException(JsonProcessingException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<>(new ErrorResponse("Invalid request JSON.", e.getMessage()), BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
		log.error(e.getMessage(), e);
		String message = e.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.findFirst()
				.orElse(e.getMessage());
		return new ResponseEntity<>(new ErrorResponse(message, e.getMessage()), BAD_REQUEST);
	}

}
