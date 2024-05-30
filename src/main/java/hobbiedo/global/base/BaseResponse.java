package hobbiedo.global.base;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import hobbiedo.global.status.ErrorStatus;
import hobbiedo.global.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
	private Boolean isSuccess;
	private String status;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public static <T> BaseResponse<T> onSuccess(SuccessStatus status, T data) {
		return new BaseResponse<>(true, String.valueOf(HttpStatus.OK.value()), status.getStatus(),
			data);
	}

	// 요청에 성공한 경우 -> return 객체가 필요 없는 경우
	public static <T> BaseResponse<T> onFailure(ErrorStatus status) {
		return new BaseResponse<>(false, status.getStatus(), status.getMessage(), null);
	}

	public static <T> BaseResponse<T> onFailure(ErrorStatus status, String message) {
		return new BaseResponse<>(false, status.getStatus(), message, null);
	}
}

