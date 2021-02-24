package com.samaelc.volcano.reservation.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.samaelc.volcano.reservation.model.ApiError;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ReservationNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(ReservationNotFoundException ex) {
		ApiError apiError = ApiError.builder()
				.status(HttpStatus.NOT_FOUND)
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler({ InvalidDatesException.class, ReservationException.class, ReservationNotAvailableException.class })
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException ex) {
		ApiError apiError = ApiError.builder()
				.status(HttpStatus.BAD_REQUEST)
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getField() + " " + x.getDefaultMessage())
				.collect(Collectors.toList());

		ApiError apiError = ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST)
				.errors(errors)
				.build();

		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		ApiError apiError = ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST)
				.message("Validation error")
				.build();
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
