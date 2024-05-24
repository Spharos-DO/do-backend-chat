package hobbiedo.chat.dto.request;

import lombok.Getter;

@Getter
public class ChatSendDTO {
	private Long crewId;
	private String uuid;
	private String text;
	private String imageUrl;
	private String videoUrl;
}
