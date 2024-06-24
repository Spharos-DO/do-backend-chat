package hobbiedo.chat.domain;

import java.time.Instant;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "ChatJoinTime")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndex(name = "uuid_crewId_idx", def = "{'uuid': 1, 'crewId': 1}")
public class ChatJoinTime {
	private String id;
	private String uuid;
	private Long crewId;
	private Instant joinTime;

	@Builder
	public ChatJoinTime(String id, String uuid, Long crewId, Instant joinTime) {
		this.id = id;
		this.uuid = uuid;
		this.crewId = crewId;
		this.joinTime = joinTime;
	}
}
