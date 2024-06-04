package hobbiedo.chat.vo.request;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import lombok.Getter;

@Getter
public class ChatSendVo {
	private Long crewId;
	private String text;
	private String imageUrl;
	private String entryExitNotice;
<<<<<<< HEAD

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
=======
>>>>>>> 8de2d9f (fix: ChatApplication 에서 @OpenAPIDefinition 제거)
}
