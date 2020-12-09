package com.galvao.permission.exception.exeptions;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

	public HttpException(String message) {
		super(message);
	}

	public abstract HttpStatus getHttpStatus();
}
