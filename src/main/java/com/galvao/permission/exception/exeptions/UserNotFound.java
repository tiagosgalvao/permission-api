package com.galvao.permission.exception.exeptions;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFound extends HttpException {

	public UserNotFound(String message) {
		super(String.format("User '%s' not found.", message));
	}

	@Override
	public HttpStatus getHttpStatus() {
		return NOT_FOUND;
	}
}
