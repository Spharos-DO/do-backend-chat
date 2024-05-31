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

@Document(collection = "Chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndex(name = "crewId_createdAt_idx", def = "{'crewId': 1, 'createdAt': 1}")
public class Chat {
	@Id
	private String id;
	private Long crewId;
	private String uuid;
	private String text;
	private String imageUrl;
	private String entryExitNotice;
	private Instant createdAt;

	@Builder
	public Chat(String id, Long crewId, String uuid, String text, String imageUrl,
		String entryExitNotice, Instant createdAt) {
		this.id = id;
		this.crewId = crewId;
		this.uuid = uuid;
		this.text = text;
		this.imageUrl = imageUrl;
		this.entryExitNotice = entryExitNotice;
		this.createdAt = createdAt;
	}
}
