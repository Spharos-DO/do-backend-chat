package hobbiedo.global.code;

import hobbiedo.global.dto.ErrorReasonDto;

public interface BaseErrorCode {
	ErrorReasonDto getReason();

	ErrorReasonDto getReasonHttpStatus();
}
