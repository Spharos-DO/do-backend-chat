package hobbiedo.base.code;
import hobbiedo.base.dto.ErrorReasonDto;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
