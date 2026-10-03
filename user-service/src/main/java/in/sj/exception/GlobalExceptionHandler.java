package in.sj.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import in.sj.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"USER_NOT_FOUND", exception.getMessage(), request.getDescription(false).replace("uri=", ""));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, WebRequest request) {
		log.error("Unexpected error occurred", exception);

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"INTERNAL_SERVER_ERROR", "Something went wrong. Please try again later.",
				request.getDescription(false).replace("uri=", ""));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception,
			WebRequest request) {

		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).findFirst()
				.orElse("Invalid request data");

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				"VALIDATION_ERROR", message, request.getDescription(false).replace("uri=", ""));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

}