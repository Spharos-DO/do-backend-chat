package hobbiedo.chat.dto.request;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import lombok.Getter;

@Getter
public class ChatSendDTO {
	private Long crewId;
	private String text;
	private String imageUrl;
	private String videoUrl;
	private String entryExitNotice;

	public Chat toEntity(String uuid) {
		return Chat.builder()
			.crewId(crewId)
			.uuid(uuid)
			.text(text)
			.imageUrl(imageUrl)
			.entryExitNotice(entryExitNotice)
			.createdAt(Instant.now())
			.build();
	}
}
