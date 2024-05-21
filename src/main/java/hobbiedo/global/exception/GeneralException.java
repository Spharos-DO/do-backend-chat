<<<<<<<< HEAD:src/main/java/hobbiedo/global/exception/GeneralException.java
package hobbiedo.global.exception;


import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.dto.ErrorReasonDTO;
========
package hobbiedo.global.base.exception;

import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.dto.ErrorReasonDto;
>>>>>>>> add8ffc (init: response entity 와 git issue, pr templete 세팅):src/main/java/hobbiedo/global/base/exception/GeneralException.java
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
