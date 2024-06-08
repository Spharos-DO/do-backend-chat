package hobbiedo.chat.dto.request;

import java.time.Instant;

import hobbiedo.chat.domain.ChatLastStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LastChatTimeDTO {
	@Schema(description = "이용자가 나가기 전, 마지막으로 조회한 채팅의 시간")
	private Instant lastChatTime;

	public ChatLastStatus toEntity(ChatLastStatus chatUnReadStatus) {
		return ChatLastStatus.builder()
			.id(chatUnReadStatus.getId())
			.uuid(chatUnReadStatus.getUuid())
			.crewId(chatUnReadStatus.getCrewId())
			.lastReadAt(lastChatTime)
			.build();
	}
}
