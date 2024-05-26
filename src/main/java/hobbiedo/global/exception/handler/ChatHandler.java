package hobbiedo.global.exception.handler;


import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.exception.GeneralException;

public class ExampleHandler extends GeneralException {
	public ExampleHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}

