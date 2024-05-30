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

@Document(collection = "ChatUnReadStatus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndex(name = "uuid_crewId_idx", def = "{'uuid': 1, 'crewId': 1}")
public class ChatUnReadStatus {
	@Id
	private String id;
	private String uuid;
	private Long crewId;
	private Instant lastReadAt;
	private Integer unreadCount;

	@Builder
	public ChatUnReadStatus(String id, String uuid, Long crewId, Instant lastReadAt, Integer unreadCount) {
		this.id = id;
		this.uuid = uuid;
		this.crewId = crewId;
		this.lastReadAt = lastReadAt;
		this.unreadCount = unreadCount;
	}
}
