package hobbiedo.global.exception;

import hobbiedo.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

	private final ErrorStatus status;

	public GlobalException(ErrorStatus status) {
		this.status = status;
	}
}
