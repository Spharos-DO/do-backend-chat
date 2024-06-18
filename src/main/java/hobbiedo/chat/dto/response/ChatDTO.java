package hobbiedo.chat.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import hobbiedo.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDTO {
	private String uuid;
	private String text;
	private String imageUrl;
	private String entryExitNotice;
	private Instant createdAt;

	public static ChatDTO toDto(Chat chat) {
		return ChatDTO.builder()
			.uuid(chat.getUuid())
			.text(chat.getText())
			.imageUrl(chat.getImageUrl())
			.entryExitNotice(chat.getEntryExitNotice())
			.createdAt(chat.getCreatedAt())
			.build();
	}
}
