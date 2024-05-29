package hobbiedo.chat.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
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
public class ChatUnReadStatus {
	@Id
	private String id;
	private String uuid;
	private Long crewId;
	private Instant lastAt;
	private Integer count;

	@Builder
	public ChatUnReadStatus(String id, String uuid, Long crewId, Instant lastAt, Integer count) {
		this.id = id;
		this.uuid = uuid;
		this.crewId = crewId;
		this.lastAt = lastAt;
		this.count = count;
	}
}
