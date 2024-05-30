package hobbiedo.chat.vo.response;

import java.time.Instant;

import lombok.Getter;

@Getter
public class LastChatInfoVO {
	private Long crewId;
	private String lastChat;
	private Instant createdAt;
}
