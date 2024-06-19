package hobbiedo.chat.kafka.dto;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.kafka.type.EntryExitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntryExitDTO {
	private long crewId;
	private String uuid;
	private EntryExitType entryExitType;

	public Chat toEntity() {
		return Chat.builder()
			.crewId(crewId)
			.uuid(uuid)
			.entryExitNotice(entryExitType.getMessage())
			.build();
	}
}
