package hobbiedo.chat.kafka.dto;

import java.time.Instant;

import hobbiedo.chat.domain.ChatLastStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatLastStatusCreateDTO {
	private long crewId;
	private String uuid;

	public ChatLastStatus toEntity() {
		return ChatLastStatus.builder()
			.uuid(uuid)
			.crewId(crewId)
			.connectionStatus(false)
			.lastReadAt(Instant.now())
			.build();
	}
}
