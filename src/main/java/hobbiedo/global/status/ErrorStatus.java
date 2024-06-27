package hobbiedo.global.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

	INTERNAL_SERVER_ERROR("SERVER500", "Internal server error"),
	BAD_REQUEST("REQUEST400", "Bad request"),

	NO_EXIST_IMAGE_CHAT("CHAT400", "조회 가능한 사진 체팅이 존재하지 않습니다."),
	NO_FIND_LAST_CHAT("CHAT401", "채팅방의 마지막 채팅 정보가 존재하지 않습니다."),
	NO_CHAT_CONTENT("CHAT402", "채팅 데이터에 내용(문자, 사진)이 존재하지 않습니다."),
	NO_FIND_CHAT_UNREAD_STATUS("CHAT405", "채팅의 마지막 상태가 존재하지 않습니다."),
	NO_EXIST_CHAT("CHAT406", "조회 가능한 체팅이 존재하지 않습니다."),
	NO_EXIST_CHAT_UNREAD_STATUS("CHAT407", "조회 가능한 체팅 읽음 상태가 존재하지 않습니다."),
	NO_EXIST_UNREAD_COUNT("CHAT408", "조회 가능한 읽지 않은 체팅 개수가 존재하지 않습니다."),
	NO_MATCH_ENTRY_EXIT_TYPE("CHAT409", "매칭되는 입퇴장 타입이 존재하지 않습니다."),
	NO_EXIST_JOIN_TIME("CHAT410", "조회 가능한 입장 시간이 존재하지 않습니다."),
	NO_EXIST_CHAT_LIST("CHAT411", "조회 가능한 채팅 리스트가 존재하지 않습니다."),;

	private final String status;
	private final String message;

}
