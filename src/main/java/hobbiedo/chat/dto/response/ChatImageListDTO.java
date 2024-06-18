package hobbiedo.chat.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatImageListDTO {
	private LocalDate date;
	private List<ChatImageDTO> chats;

	public static ChatImageListDTO toDto(LocalDate date, List<ChatImageDTO> chats) {
		return ChatImageListDTO.builder()
			.date(date)
			.chats(chats)
			.build();
	}
}
