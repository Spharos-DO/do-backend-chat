package hobbiedo.chat.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
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
public class Chat {
	@Id
	private String id;
	private Long crewId;
	private String uuid;
	private String text;
	private String imageUrl;
	private String videoUrl;
	private String entryExitNotice;
	private Instant createdAt;

	@Builder
	public Chat(String id, Long crewId, String uuid, String text, String imageUrl,
		String videoUrl,
		String entryExitNotice, Instant createdAt) {
		this.id = id;
		this.crewId = crewId;
		this.uuid = uuid;
		this.text = text;
		this.imageUrl = imageUrl;
		this.videoUrl = videoUrl;
		this.entryExitNotice = entryExitNotice;
		this.createdAt = createdAt;
	}
}
