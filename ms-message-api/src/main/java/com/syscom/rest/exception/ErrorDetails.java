package com.syscom.rest.exception;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
//@Builder
@ToString
//@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ErrorDetails {

	public ErrorDetails(LocalDateTime now, String message, String details) {
		this.timestamp = now;
		this.message = message;
		this.details = details;
	}

	private LocalDateTime timestamp;
	private String message;
	private String details;

}
