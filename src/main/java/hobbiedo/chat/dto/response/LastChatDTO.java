package hobbiedo.chat.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatDTO {
	private Long crewId;
	private String lastChatContent;
	private Integer unreadCount;
	private Instant createdAt;

	public static LastChatDTO toDto(Long crewId, String lastChatContent, Integer unreadCount, Instant createdAt) {
		return LastChatDTO.builder()
			.crewId(crewId)
			.lastChatContent(lastChatContent)
			.unreadCount(unreadCount)
			.createdAt(createdAt)
			.build();
	}
}
