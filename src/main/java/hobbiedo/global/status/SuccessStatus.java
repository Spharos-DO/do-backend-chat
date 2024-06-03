package hobbiedo.global.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus { //메시지를 중앙에서 관리하는 것이 유지 보수와 일관성 측면에서 유리
	FIND_CHAT_CONTENT("채팅 내용 조회에 성공하였습니다."),
	CREATE_CHAT("새로운 채팅이 생성되었습니다."),
	FIND_LAST_CHAT("채팅방 리스트에서의 최근 채팅 내용 조회에 성공하였습니다."),
	UPDATE_LAST_READ_AT("마지막 읽은 채팅 시간을 수정하였습니다."),
	FIND_UNREAD_COUNT("안 읽은 채팅 개수 조회에 성공하였습니다.");

	private final String status;
}
