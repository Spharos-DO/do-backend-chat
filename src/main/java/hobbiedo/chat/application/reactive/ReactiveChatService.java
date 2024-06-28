package hobbiedo.chat.application.reactive;

import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatDTO;
import hobbiedo.chat.dto.response.LastChatDTO;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveChatService {

	Mono<Void> sendChat(ChatSendDTO chatSendDTO, String uuid);

	Flux<ChatDTO> getStreamChat(Long crewId, String uuid);

	Flux<String> subscribeToChannelByUuid(String uuid);

	Flux<LastChatDTO> getLatestChatAndStream(Long crewId, String uuid);

	Mono<Void> updateLastStatusAt(LastStatusModifyDTO lastStatusModifyDTO, String uuid);

	Mono<Void> createEntryChatAndJoinTime(CrewEntryExitDTO entryExitDTO);

	Mono<Void> createExitChatAndDeleteJoinTime(CrewEntryExitDTO entryExitDTO);

	Mono<Void> deleteUnreadCount(CrewEntryExitDTO entryExitDTO);
}
