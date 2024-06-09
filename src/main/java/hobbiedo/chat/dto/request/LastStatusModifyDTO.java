package hobbiedo.chat.dto.request;

import java.time.Instant;

import hobbiedo.chat.domain.ChatLastStatus;
import lombok.Getter;

@Getter
public class LastStatusModifyDTO {
	private Long crewId;
	private boolean connectionStatus;
	private Instant lastReadAt;

	public ChatLastStatus toEntity(ChatLastStatus chatUnReadStatus) {
		return ChatLastStatus.builder()
			.id(chatUnReadStatus.getId())
			.uuid(chatUnReadStatus.getUuid())
			.crewId(chatUnReadStatus.getCrewId())
			.connectionStatus(connectionStatus)
			.lastReadAt(lastReadAt == null ? chatUnReadStatus.getLastReadAt() : lastReadAt)
			.build();
	}
}
