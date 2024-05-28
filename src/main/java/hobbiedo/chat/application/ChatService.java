package hobbiedo.chat.application;

import java.time.LocalDateTime;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.vo.request.ChatSendVo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService {

	Mono<Chat> sendChat(ChatSendVo chatSendVo, String uuid);

	Flux<Chat> getChatByCrewIdAfterDateTime(String crewId, LocalDateTime since);
}
