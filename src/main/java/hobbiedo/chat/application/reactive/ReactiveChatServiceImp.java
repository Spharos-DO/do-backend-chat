package hobbiedo.chat.application.reactive;

import java.time.Instant;

import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatStreamDTO;
import hobbiedo.chat.dto.response.LastChatInfoDTO;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatLastStatusRepository;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatRepository;
import hobbiedo.global.exception.GlobalException;
import hobbiedo.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReactiveChatServiceImp implements ReactiveChatService {
	private final ReactiveChatRepository chatRepository;
	private final ReactiveChatLastStatusRepository chatLastStatusRepository;
	private final UnreadCountService unreadCountService;

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
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastReadAt()))
			.map(ChatStreamDTO::toDto);
	}

	public Flux<LastChatInfoDTO> getStreamLatestChat(Long crewId, String uuid) {
		return chatRepository.findLatestByCrewId(crewId)
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_LAST_CHAT)))
			.flatMapMany(latestChat -> {
				Instant latestChatCreatedAt = latestChat.getCreatedAt();
				return chatLastStatusRepository.findByCrewIdAndUuid(crewId, uuid)
					.switchIfEmpty(
						Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
					.flatMapMany(chatLastStatus -> {
						Instant lastReadAt = chatLastStatus.getLastReadAt();
						return chatRepository.countByCrewIdAndCreatedAtBetween(crewId, lastReadAt,
								latestChatCreatedAt)
							.flatMap(count -> unreadCountService.initializeUnreadCount(crewId, uuid,
								count))
							.thenMany(processNewChats(crewId, uuid, latestChatCreatedAt,
								chatLastStatus.getId()));
					});
			});
	}

	private Flux<LastChatInfoDTO> processNewChats(Long crewId, String uuid,
		Instant latestChatCreatedAt, String chatLastStatusId) {
		return chatRepository.findByCrewIdAndCreatedAtAfter(crewId, latestChatCreatedAt)
			.flatMap(chat -> unreadCountService.incrementUnreadCount(crewId, uuid)
				.thenReturn(chat))
			.flatMap(chat -> unreadCountService.getUnreadCount(crewId, uuid)
				.flatMap(unreadCount -> chatLastStatusRepository.findById(chatLastStatusId)
					.switchIfEmpty(
						Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
					.flatMap(chatLastStatus -> setLastChatContent(chat)
							.map(lastChatContent -> LastChatInfoDTO.toDTO(chat, lastChatContent,
								chatLastStatus.isConnectionStatus() ? 0 : unreadCount.intValue()))
						// 0: 접속중
					)
				)
			);
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
	public Mono<Void> updateLastStatusAt(LastStatusModifyDTO lastStatusModifyDTO, String uuid) {
		return chatLastStatusRepository.findByCrewIdAndUuid(lastStatusModifyDTO.getCrewId(), uuid)
			.flatMap(chatUnReadStatus -> chatLastStatusRepository
				.save(lastStatusModifyDTO.toEntity(chatUnReadStatus)))
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
			.onErrorMap(e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
			.then();
	}

	// public Flux<LastChatInfoDTO> getStreamLatestChat(Long crewId) {
	// 	return chatRepository.findLatestByCrewId(crewId)
	// 		.flatMapMany(latestChat ->
	// 			chatRepository.findByCrewIdAndCreatedAtAfter(crewId, latestChat.getCreatedAt())
	// 		)
	// 		.flatMap(chat -> setLastChatContent(chat)
	// 			.map(lastChatContent -> LastChatInfoDTO.toDTO(chat, lastChatContent)));
	// }

	// @Override
	// public Mono<UnReadCountDTO> getUnreadCount(Long crewId, String uuid) {
	// 	return chatUnReadStatusRepository.findByCrewIdAndUuid(crewId, uuid)
	// 		.map(UnReadCountDTO::toDTO)
	// 		.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)));
	// }
}
