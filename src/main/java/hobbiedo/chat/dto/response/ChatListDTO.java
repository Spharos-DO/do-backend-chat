package hobbiedo.chat.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatListDTO {
	private LocalDate date;
	private List<ChatDTO> chats;

	public static ChatListDTO toDto(LocalDate date, List<ChatDTO> chats) {
		return ChatListDTO.builder()
			.date(date)
			.chats(chats)
			.build();
	}
}
