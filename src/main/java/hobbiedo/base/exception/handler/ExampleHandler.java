package hobbiedo.base.exception.handler;

import hobbiedo.base.code.BaseErrorCode;
import hobbiedo.base.exception.GeneralException;

public class ExampleHandler extends GeneralException {
	public ExampleHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
