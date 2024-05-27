package hobbiedo.global.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import hobbiedo.global.base.ApiResponse;
import hobbiedo.global.base.code.status.ErrorStatus;
import hobbiedo.global.base.dto.ErrorReasonDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

	@ExceptionHandler(ConstraintViolationException.class)
	public Mono<ApiResponse<Object>> handleConstraintViolationException(
		ConstraintViolationException ex, ServerWebExchange exchange) {
		String errorMessage = ex.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.findFirst()
			.orElseThrow(() -> new RuntimeException("ConstraintViolation 추출 오류 발생"));

		return buildErrorResponse(ErrorStatus.VALID_EXCEPTION.getStatus(), errorMessage,
			ErrorStatus.VALID_EXCEPTION.getHttpStatus());
	}

	@ExceptionHandler(GeneralException.class)
	public Mono<ApiResponse<Object>> handleGeneralException(GeneralException ex,
		ServerWebExchange exchange) {
		ErrorReasonDTO errorReasonDto = ex.getErrorReasonHttpStatus();
		ApiResponse<Object> body = ApiResponse.onFailure(errorReasonDto.getCode(),
			errorReasonDto.getMessage(), null);
		return buildErrorResponse(body, errorReasonDto.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Mono<ApiResponse<Object>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException ex, ServerWebExchange exchange) {
		List<String> errorMessages = ex.getBindingResult()
			.getAllErrors()
			.stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.toList());

		String errorMessage = String.join(", ", errorMessages);
		return buildErrorResponse(ErrorStatus.VALID_EXCEPTION.getStatus(), errorMessage,
			HttpStatus.BAD_REQUEST);
	}

	private Mono<ApiResponse<Object>> buildErrorResponse(String status, String message,
		HttpStatus httpStatus) {
		ApiResponse<Object> response = ApiResponse.onFailure(status, message, null);
		return Mono.just(response);
	}

	private Mono<ApiResponse<Object>> buildErrorResponse(ApiResponse<Object> response,
		HttpStatus httpStatus) {
		return Mono.just(response);
	}
}
