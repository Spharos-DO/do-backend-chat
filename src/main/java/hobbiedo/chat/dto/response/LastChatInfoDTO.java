<<<<<<< HEAD:src/main/java/hobbiedo/chat/vo/response/LastChatInfoVO.java
// package hobbiedo.chat.dto.response;
//
// import java.time.Instant;
//
// import hobbiedo.chat.domain.Chat;
// import lombok.Builder;
// import lombok.Getter;
//
// @Getter
// @Builder
// public class LastChatInfoDTO {
// 	private Long crewId;
// 	private String lastChatContent;
// 	private Instant createdAt;
//
// 	public static LastChatInfoDTO toDTO(Chat chat, String lastChatContent) {
// 		return LastChatInfoDTO.builder()
// 			.crewId(chat.getCrewId())
// 			.lastChatContent(lastChatContent)
// 			.createdAt(chat.getCreatedAt())
// 			.build();
// 	}
// }
=======
package hobbiedo.chat.vo.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LastChatInfoVO {
	private Long crewId;
	private String lastChatContent;
	private Instant createdAt;
}
>>>>>>> 8de2d9f (fix: ChatApplication 에서 @OpenAPIDefinition 제거):src/main/java/hobbiedo/chat/dto/response/LastChatInfoDTO.java
