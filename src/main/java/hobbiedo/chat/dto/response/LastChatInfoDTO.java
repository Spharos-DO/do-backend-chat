package hobbiedo.chat.dto.response;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatInfoDTO {
	private Long crewId;
	private String lastChatContent;
	private int unreadCount;
	private Instant createdAt;

	public static LastChatInfoDTO toDTO(Chat chat, String lastChatContent, int unreadCount) {
		return LastChatInfoDTO.builder()
			.crewId(chat.getCrewId())
			.lastChatContent(lastChatContent)
			.unreadCount(Math.min(unreadCount, 999)) // 최대 999개까지만 표시
			.createdAt(chat.getCreatedAt())
			.build();
	}
}

