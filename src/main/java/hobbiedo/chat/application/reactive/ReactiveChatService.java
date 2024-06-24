package hobbiedo.chat.application.reactive;

import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatStreamDTO;
import hobbiedo.chat.dto.response.LastChatInfoDTO;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveChatService {

	Mono<Void> sendChat(ChatSendDTO chatSendDTO, String uuid);

	Flux<ChatStreamDTO> getStreamChat(Long crewId, String uuid);

	Flux<LastChatInfoDTO> getStreamLatestChat(Long crewId, String uuid);

	Mono<Void> updateLastStatusAt(LastStatusModifyDTO lastStatusModifyDTO, String uuid);

	Mono<Void> sendEntryExitChat(CrewEntryExitDTO entryExitDTO);

	Mono<Void> createUnreadCount(CrewEntryExitDTO entryExitDTO);

	Mono<Void> deleteUnreadCount(CrewEntryExitDTO entryExitDTO);
}
