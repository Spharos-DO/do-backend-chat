// package hobbiedo.global.exception;
//
// import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.thoughtworks.xstream.core.BaseException;
//
// import hobbiedo.global.base.BaseResponse;
// import hobbiedo.global.status.ErrorStatus;
// import lombok.RequiredArgsConstructor;
// import reactor.core.publisher.Mono;
//
// @Order(-1)
// @RequiredArgsConstructor
// @Component
// public class ErrorResponse implements ErrorWebExceptionHandler {
// 	private final ObjectMapper objectMapper;
//
// 	@Override
// 	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
// 		ServerHttpResponse response = exchange.getResponse();
//
// 		BaseResponse<?> baseResponse;
// 		if (ex instanceof GlobalException e) {
// 			baseResponse = BaseResponse.onFailure(e.getStatus());
// 			response.setStatusCode(HttpStatus.OK);
// 		} else {
// 			baseResponse = BaseResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
// 			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
// 		}
//
// 		//response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
// 		try {
// 			return response
// 				.writeWith(Mono.just(response.bufferFactory()
// 					.wrap(objectMapper.writeValueAsBytes(baseResponse))));
// 		} catch (JsonProcessingException e) {
// 			return Mono.error(e);
// 		}
// 	}
// }
