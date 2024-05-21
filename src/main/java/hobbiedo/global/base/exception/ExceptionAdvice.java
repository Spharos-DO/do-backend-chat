package hobbiedo.global.base.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import hobbiedo.global.base.ApiResponse;
import hobbiedo.global.base.code.status.ErrorStatus;
import hobbiedo.global.base.dto.ErrorReasonDto;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	/**
	 * @return DB 테이블 유효성 대한 검증 에러 응답 처리
	 */
	@ExceptionHandler
	public ResponseEntity<Object> dbValidationException(ConstraintViolationException e,
			WebRequest request) {
		String errorMessage = e.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("ConstrainViolation 추출 오류 발생"));

		return handleExceptionInternalConstraint(
				e,
				errorMessage,
				HttpHeaders.EMPTY,
				request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleCustomException(jp.api.exception.GeneralException ex,
			WebRequest request) {
		ErrorReasonDto errorReasonDto = ex.getErrorReasonHttpStatus();
		ApiResponse<Object> body = ApiResponse.onFailure(errorReasonDto.getCode(),
				errorReasonDto.getMessage(), null);
		return toResponseEntity(ex, HttpHeaders.EMPTY, request, errorReasonDto.getHttpStatus(),
				body);
	}
	//
	// @ExceptionHandler(value = MethodArgumentNotValidException.class)
	// public ResponseEntity<Object> handleMethodArgumentNotValid(
	// 	MethodArgumentNotValidException ex, WebRequest request) {
	// 	String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
	// 	ApiResponse<Object> body = ApiResponse.onFailure(ex.getStatusCode().toString(), errorMessage,
	// 		null);
	// 	return toResponseEntity(ex, HttpHeaders.EMPTY, request, HttpStatus.BAD_REQUEST, body);
	// }

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		log.info("ResponseEntity = {}", status);
		ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);

		ApiResponse<Object> body = ApiResponse.onFailure(
				objectError.getCode(),
				objectError.getDefaultMessage(),
				null);

		return toResponseEntity(
				ex,
				HttpHeaders.EMPTY,
				request,
				HttpStatus.BAD_REQUEST,
				body);
	}

	/**
	 * @return @Valid 검증에 대한 에러 응답 처리
	 */

	private ResponseEntity<Object> handleExceptionInternalConstraint(
			Exception ex, String errorMessage, HttpHeaders headers, WebRequest request) {

		ApiResponse<Object> body = ApiResponse.onFailure(
				ErrorStatus.VALID_EXCEPTION.getStatus(),
				errorMessage,
				null);

		return toResponseEntity(ex, headers, request, ErrorStatus.VALID_EXCEPTION.getHttpStatus(),
				body);
	}

	private ResponseEntity<Object> toResponseEntity(Exception e, HttpHeaders headers,
			WebRequest request,
			HttpStatus httpStatus, ApiResponse<Object> body) {
		return super.handleExceptionInternal(
				e,
				body,
				headers,
				httpStatus,
				request
		);
	}
}
