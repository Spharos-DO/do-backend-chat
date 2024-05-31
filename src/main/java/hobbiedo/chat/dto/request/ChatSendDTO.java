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

	public Chat fromEntity(String uuid) {
		return Chat.builder()
			.crewId(getCrewId())
			.uuid(uuid)
			.text(getText())
			.imageUrl(getImageUrl())
			.videoUrl(getVideoUrl())
			.entryExitNotice(getEntryExitNotice())
			.createdAt(Instant.now())
			.build();
	}
}
