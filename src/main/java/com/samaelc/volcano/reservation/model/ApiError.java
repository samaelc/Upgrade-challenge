package com.samaelc.volcano.reservation.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {
	private HttpStatus status;
	private String message;
	private LocalDateTime timestamp;
	private List<String> errors;
}
