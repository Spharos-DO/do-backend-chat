package hobbiedo.chat.dto.response;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatListDTO {
	private long crewId;
	private String lastChatContent;
	private Integer unreadCount;
	private Instant createdAt;

	public static LastChatListDTO toDto(Chat chat, String lastChatContent, Integer unreadCount) {
		return LastChatListDTO.builder()
			.crewId(chat.getCrewId())
			.lastChatContent(lastChatContent)
			.unreadCount(unreadCount)
			.createdAt(chat.getCreatedAt())
			.build();
	}
}

