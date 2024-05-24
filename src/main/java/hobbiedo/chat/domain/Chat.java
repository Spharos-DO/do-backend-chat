package hobbiedo.chat.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;

@Document(collection = "chat")
@Getter
@Builder
public class Chat {
	@Id
	private Long id;
	private Long crewId;
	private String uuid;
	private String text;
	private String imageUrl;
	private String videoUrl;
	private String entryExitNotice;
	private LocalDateTime createdAt;
}
