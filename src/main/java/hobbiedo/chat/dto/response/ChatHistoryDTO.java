package hobbiedo.chat.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatHistoryDTO {
	private int lastPage;
	private List<ChatListDTO> chatList;

	public static ChatHistoryDTO toDto(int lastPage, List<ChatListDTO> chatList) {
		return ChatHistoryDTO.builder()
			.lastPage(lastPage)
			.chatList(chatList)
			.build();
	}
}
