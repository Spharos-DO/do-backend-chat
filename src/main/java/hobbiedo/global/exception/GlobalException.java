package hobbiedo.global.exception;

import hobbiedo.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
	private final ErrorStatus status;

	public GlobalException(ErrorStatus status) {
		super(status.getMessage());
		this.status = status;
	}

	public GlobalException(ErrorStatus status, String message) {
		super(message);
		this.status = status;
	}
}
