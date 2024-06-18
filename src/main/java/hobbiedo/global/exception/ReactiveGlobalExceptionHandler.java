package hobbiedo.global.exception;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import hobbiedo.global.base.BaseResponse;
import hobbiedo.global.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ReactiveGlobalExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	protected Mono<BaseResponse<?>> baseError(GlobalException ex, ServerHttpRequest request) {
		log.error("errorStatus: {}, url: {}, message: {}", ex.getStatus(), request.getURI(),
			ex.getMessage());
		return Mono.just(BaseResponse.onFailure(ex.getStatus(), ex.getMessage()));
	}

	// RuntimeException의 경우 일반적인 에러 메시지를 사용하여 보안 유지
	@ExceptionHandler(RuntimeException.class)
	protected Mono<BaseResponse<?>> runtimeError(RuntimeException ex, ServerHttpRequest request) {
		log.error("url: {}, message: {}", request.getURI(), ex.getMessage());
		return Mono.just(
			BaseResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<BaseResponse<?>> processValidationError(WebExchangeBindException ex,
		ServerHttpRequest request) {
		log.error("url: {}, message: {}", request.getURI(), ex.getMessage());

		StringBuilder builder = new StringBuilder();
		ex.getFieldErrors().forEach(fieldError -> {
			builder.append(fieldError.getDefaultMessage());
			builder.append(" / ");
		});

		return Mono.just(BaseResponse.onFailure(ErrorStatus.BAD_REQUEST, builder.toString()));
	}
}
