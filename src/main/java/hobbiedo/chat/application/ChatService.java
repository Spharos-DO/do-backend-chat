package hobbiedo.chat.application;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.dto.request.ChatSendDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService {

	Mono<Chat> sendChat(ChatSendDTO chatSendVo, String uuid);

	Flux<Chat> getStreamChat(Long crewId, String uuid);

	//Flux<LastChatInfoDTO> getLatestChats(String uuid);
}
