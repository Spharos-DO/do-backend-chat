package hobbiedo.chat.vo.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatInfoVO {
	private Long crewId;
	private String lastChatContent;
	private Instant createdAt;
}
