// package hobbiedo.chat.dto.response;
//
// import hobbiedo.chat.domain.ChatUnReadStatus;
// import lombok.Builder;
// import lombok.Getter;
//
// @Getter
// @Builder
// public class UnReadCountDTO {
// 	private Long crewId;
// 	private Integer unreadCount;
//
// 	public static UnReadCountDTO toDTO(ChatUnReadStatus chatUnReadStatus) {
// 		return UnReadCountDTO.builder()
// 			.crewId(chatUnReadStatus.getCrewId())
// 			.unreadCount(chatUnReadStatus.getUnreadCount())
// 			.build();
// 	}
// }
