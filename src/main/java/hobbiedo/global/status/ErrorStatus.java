package hobbiedo.global.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

	INTERNAL_SERVER_ERROR("SERVER500", "Internal server error"),
	BAD_REQUEST("REQUEST400", "Bad request"),

	NO_CHAT_CONTENT("CHAT402", "채팅 데이터에 내용(문자, 사진, 비디오)이 존재하지 않습니다.");

	private final String status;
	private final String message;

}
