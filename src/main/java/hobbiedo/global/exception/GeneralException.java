<<<<<<< HEAD
<<<<<<<< HEAD:src/main/java/hobbiedo/global/exception/GeneralException.java
package hobbiedo.global.exception;


import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.dto.ErrorReasonDTO;
========
package hobbiedo.global.base.exception;

import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.dto.ErrorReasonDto;
>>>>>>>> 9d7fb0a (init: response entity 와 git issue, pr templete 세팅):src/main/java/hobbiedo/global/base/exception/GeneralException.java
=======
package hobbiedo.global.exception;

import hobbiedo.global.code.BaseErrorCode;
import hobbiedo.global.dto.ErrorReasonDto;
>>>>>>> b989396 (eureka client 적요)
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
	private BaseErrorCode errorCode;

<<<<<<< HEAD
	public ErrorReasonDTO getErrorReason() {
		return errorCode.getReason();
	}

	public ErrorReasonDTO getErrorReasonHttpStatus() {
=======
	public ErrorReasonDto getErrorReason() {
		return errorCode.getReason();
	}

	public ErrorReasonDto getErrorReasonHttpStatus() {
>>>>>>> b989396 (eureka client 적요)
		return errorCode.getReasonHttpStatus();
	}
}
