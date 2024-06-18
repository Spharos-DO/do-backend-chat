package hobbiedo.chat.dto.response;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatImageDTO {
	private String uuid;
	private String imageUrl;
	private Instant createdAt;

	public static ChatImageDTO toDto(Chat chat) {
		return ChatImageDTO.builder()
			.uuid(chat.getUuid())
			.imageUrl(chat.getImageUrl())
			.createdAt(chat.getCreatedAt())
			.build();
	}
}
