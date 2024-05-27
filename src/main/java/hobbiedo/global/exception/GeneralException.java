package hobbiedo.global.exception;


import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
	private BaseErrorCode errorCode;

	public ErrorReasonDTO getErrorReason() {
		return errorCode.getReason();
	}

	public ErrorReasonDTO getErrorReasonHttpStatus() {
		return errorCode.getReasonHttpStatus();
	}
}
