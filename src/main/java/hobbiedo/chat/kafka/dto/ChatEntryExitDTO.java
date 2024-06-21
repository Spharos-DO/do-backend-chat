package hobbiedo.chat.kafka.dto;

import java.time.Instant;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.domain.ChatLastStatus;
import hobbiedo.chat.kafka.type.EntryExitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntryExitDTO {
	private long crewId;
	private String uuid;
	private EntryExitType entryExitType;

	public Chat toChatEntity() {
		return Chat.builder()
			.crewId(crewId)
			.uuid(uuid)
			.entryExitNotice(entryExitType.getMessage())
			.createdAt(Instant.now())
			.build();
	}

	public ChatLastStatus toChatLastStatusEntity() {
		return ChatLastStatus.builder()
			.uuid(uuid)
			.crewId(crewId)
			.connectionStatus(false)
			.lastReadAt(Instant.now())
			.build();
	}
}
