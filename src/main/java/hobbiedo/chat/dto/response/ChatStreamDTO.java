package hobbiedo.chat.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import hobbiedo.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatStreamDTO {
	private String uuid;
	private String text;
	private String imageUrl;
	private String entryExitNotice;
	private Instant createdAt;

	public static ChatStreamDTO toDto(Chat chat) {
		return ChatStreamDTO.builder()
			.uuid(chat.getUuid())
			.text(chat.getText())
			.imageUrl(chat.getImageUrl())
			.entryExitNotice(chat.getEntryExitNotice())
			.createdAt(chat.getCreatedAt())
			.build();
	}
}
