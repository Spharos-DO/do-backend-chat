package hobbiedo.chat.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "chat")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chat {
	@Id
	private String id;
	private String crewId;
	private String uuid;
	private String text;
	private String imageUrl;
	private String videoUrl;
	private String entryExitNotice;
	private LocalDateTime createdAt;

	@Builder
	public Chat(String id, String crewId, String uuid, String text, String imageUrl, String videoUrl,
		String entryExitNotice, LocalDateTime createdAt) {
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
