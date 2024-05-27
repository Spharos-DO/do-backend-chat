package hobbiedo.global.base.code;

import hobbiedo.global.base.dto.ErrorReasonDTO;

public interface BaseErrorCode {
	ErrorReasonDTO getReason();

	ErrorReasonDTO getReasonHttpStatus();
}
