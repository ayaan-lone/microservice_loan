package com.microservice.loan.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class LoanApplicationExceptionHandler {

    @ExceptionHandler(value = { LoanApplicationException.class })
    ResponseEntity<Object> handleTransactionException(LoanApplicationException loanApplicationException) {
        return ResponseEntity.status(loanApplicationException.getHttpStatus())
                .body(loanApplicationException.getMessage());
    }
	@ExceptionHandler(value = { HttpClientErrorException.class })
	ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
		return ResponseEntity.status(httpClientErrorException.getStatusCode())
				.body(httpClientErrorException.getMessage());
	}
}
