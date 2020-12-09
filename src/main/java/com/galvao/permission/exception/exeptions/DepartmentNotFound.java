package com.galvao.permission.exception.exeptions;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DepartmentNotFound extends HttpException {

	public DepartmentNotFound(String message) {
		super(String.format("Department '%s' not found.", message));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return NOT_FOUND;
	}
}
