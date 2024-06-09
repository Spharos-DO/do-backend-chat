package hobbiedo.chat.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "ChatLastStatus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndex(name = "uuid_crewId_idx", def = "{'uuid': 1, 'crewId': 1}")
public class ChatLastStatus {
	@Id
	private String id;
	private String uuid;
	private Long crewId;
	private boolean connectionStatus;
	private Instant lastReadAt;

	@Builder
	public ChatLastStatus(String id, String uuid, Long crewId, boolean connectionStatus,
		Instant lastReadAt) {
		this.id = id;
		this.uuid = uuid;
		this.crewId = crewId;
		this.connectionStatus = connectionStatus;
		this.lastReadAt = lastReadAt;
	}
}
