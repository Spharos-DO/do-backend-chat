package hobbiedo.global.base.exception.handler;

import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.base.exception.GeneralException;

public class ExampleHandler extends GeneralException {
	public ExampleHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
