package hobbiedo.global.base;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({"isSuccess", "status", "message", "data"})
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

	private static final String DEFAULT_SUCCESS_MESSAGE = "메시지 수신 성공";

	@JsonProperty("isSuccess")
	private final Boolean isSuccess;
	private final String status;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public static <T> ApiResponse<T> onSuccess(SuccessStatus code, T data) {
		return new ApiResponse<>(
			true,
			String.valueOf(HttpStatus.OK.value()),
			code.getMessage(),
			data);
	}

	public static <T> ApiResponse<T> onFailure(String status, String message, T data) {
		return new ApiResponse<>(false, status, message, data);
	}

	public static <T> ApiResponse<T> onFailure(BaseErrorCode errorCode, T data) {
		return new ApiResponse<>(
			false,
			errorCode.getReasonHttpStatus().getCode(),
			errorCode.getReasonHttpStatus().getMessage(),
			data);
	}

}
