package hobbiedo.global.exception.handler;


import hobbiedo.global.base.code.BaseErrorCode;
import hobbiedo.global.exception.GeneralException;

public class ChatHandler extends GeneralException {
	public ChatHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}

