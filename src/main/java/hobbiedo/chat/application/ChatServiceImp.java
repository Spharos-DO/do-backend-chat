package hobbiedo.chat.application;

import java.time.Instant;

import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastChatTimeDTO;
import hobbiedo.chat.dto.response.ChatStreamDTO;
import hobbiedo.chat.dto.response.LastChatInfoDTO;
import hobbiedo.chat.infrastructure.ChatLastStatusRepository;
import hobbiedo.chat.infrastructure.ChatRepository;
import hobbiedo.global.exception.GlobalException;
import hobbiedo.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ChatServiceImp implements ChatService {
	private final ChatRepository chatRepository;
	private final ChatLastStatusRepository chatLastStatusRepository;

	@Override
	public Mono<Void> sendChat(ChatSendDTO chatSendDTO,
		String uuid) { // 응답을 void 로 보내면 error를 잡을 수 없어서 chat으로 return
		return chatRepository.save(chatSendDTO.toEntity(uuid))
			.then()
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Flux<ChatStreamDTO> getStreamChat(Long crewId, String uuid) {
		return chatLastStatusRepository.findLastReadAtByCrewIdAndUuid(crewId, uuid)
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastReadAt()))
			.map(ChatStreamDTO::toDto);
	}

	@Override
	public Flux<LastChatInfoDTO> getStreamLatestChat(Long crewId, Instant lastChatAt) {
		return chatRepository.findByCrewIdAndCreatedAtAfter(crewId, lastChatAt)
			.flatMap(chat -> setLastChatContent(chat)
				.map(lastChatContent -> LastChatInfoDTO.toDTO(chat, lastChatContent)));
	}

	private Mono<String> setLastChatContent(Chat chat) {
		return Mono.justOrEmpty(chat.getText())
			.switchIfEmpty(
				Mono.justOrEmpty(chat.getImageUrl())
					.map(url -> "사진을 보냈습니다.")
					.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_CHAT_CONTENT)))
			);
	}

	@Override
	public Mono<Void> updateLastReadAt(String uuid, Long crewId,
		LastChatTimeDTO lastChatTimeDTO) {
		return chatLastStatusRepository.findByCrewIdAndUuid(crewId, uuid)
			.flatMap(chatUnReadStatus -> chatLastStatusRepository
				.save(lastChatTimeDTO.toEntity(chatUnReadStatus)))
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
			.onErrorMap(e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
			.then();
	}

	// @Override
	// public Mono<UnReadCountDTO> getUnreadCount(Long crewId, String uuid) {
	// 	return chatUnReadStatusRepository.findByCrewIdAndUuid(crewId, uuid)
	// 		.map(UnReadCountDTO::toDTO)
	// 		.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)));
	// }
}
