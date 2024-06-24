package hobbiedo.chat.application.mvc;

import java.util.List;

import hobbiedo.chat.dto.response.ChatHistoryDTO;
import hobbiedo.chat.dto.response.ChatImageListDTO;
import hobbiedo.chat.dto.response.LastChatDTO;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;

public interface ChatService {

	ChatHistoryDTO getChatHistoryBefore(Long crewId, String uuid, int page);

	List<LastChatDTO> getChatList(String uuid);

	List<ChatImageListDTO> getChatsWithImageUrl(Long crewId);

	void createChatStatus(CrewEntryExitDTO chatEntryExitDTO);

	void deleteChatStatus(CrewEntryExitDTO chatEntryExitDTO);

	void deleteOldChatsWithImageUrl();
}
