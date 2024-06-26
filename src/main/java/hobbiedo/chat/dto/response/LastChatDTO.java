package hobbiedo.chat.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatDTO {
	private String lastChatContent;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer unreadCount;
	private Instant createdAt;

	public static LastChatDTO toDTO(String lastChatContent, Integer unreadCount,
		Instant createdAt) {
		return LastChatDTO.builder()
			.lastChatContent(lastChatContent)
			.unreadCount(unreadCount)
			.createdAt(createdAt)
			.build();
	}
}

