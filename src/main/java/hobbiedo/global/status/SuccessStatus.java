package hobbiedo.global.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus { //메시지를 중앙에서 관리하는 것이 유지 보수와 일관성 측면에서 유리
	FIND_CHAT_CONTENT("채팅 내용 조회에 성공하였습니다."),
	CREATE_CHAT("새로운 채팅이 생성되었습니다."),
	UPDATE_CONNECTION_STATUS("한 유저의 특정 소모임에 대한 접속 여부를 변경하였습니다."),
	FIND_CHAT_HISTORY("이전 채팅 내용 조회에 성공하였습니다."),
	FIND_IMAGE_CHAT("사진 모아보기 조회에 성공하였습니다."),
	FIND_LAST_CHAT("마지막 채팅 내역 조회에 성공하였습니다.");

	private final String status;
}
