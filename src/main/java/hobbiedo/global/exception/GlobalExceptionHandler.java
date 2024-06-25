
package hobbiedo.global.exception;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hobbiedo.global.base.BaseResponse;
import hobbiedo.global.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GlobalExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public BaseResponse<?> baseError(GlobalException ex) {
		log.error("errorStatus: {}, message: {}", ex.getStatus(), ex.getMessage());
		return BaseResponse.onFailure(ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public BaseResponse<?> exceptionError(Exception ex) {
		log.error("message: {}", ex.getMessage());
		return BaseResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	// // RuntimeException의 경우 일반적인 에러 메시지를 사용하여 보안 유지해야 함
	// @ExceptionHandler(RuntimeException.class)
	// public BaseResponse<?> runtimeError(RuntimeException ex, HttpServletRequest request) {
	// 	log.error("url: {}, message: {}", request.getRequestURI(), ex.getMessage());
	// 	return BaseResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	// }
	//
	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// public BaseResponse<?> processValidationError(MethodArgumentNotValidException ex,
	// 	HttpServletRequest request) {
	// 	log.error("url: {}, message: {}", request.getRequestURI(), ex.getMessage());
	//
	// 	StringBuilder builder = new StringBuilder();
	// 	ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
	// 		builder.append(fieldError.getDefaultMessage());
	// 		builder.append(" / ");
	// 	});
	//
	// 	return BaseResponse.onFailure(ErrorStatus.BAD_REQUEST, builder.toString());
	// }
}

