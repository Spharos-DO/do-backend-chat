package hobbiedo.global.base.code;
import hobbiedo.global.base.dto.ErrorReasonDto;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
