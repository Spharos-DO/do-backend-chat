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

