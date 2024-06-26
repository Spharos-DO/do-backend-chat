package hobbiedo.chat.application.reactive;

import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatDTO;
import hobbiedo.chat.dto.response.LastChatDTO;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatJoinTimeRepository;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatLastStatusRepository;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatRepository;
import hobbiedo.chat.kafka.dto.ChatEntryExitDTO;
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
	private final ReactiveChatJoinTimeRepository chatJoinTimeRepository;

	@Override
	public Mono<Void> sendChat(ChatSendDTO chatSendDTO,
		String uuid) { // 응답을 void 로 보내면 error를 잡을 수 없어서 chat으로 return
		return chatRepository.save(chatSendDTO.toEntity(uuid))
			.then()
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Flux<ChatDTO> getStreamChat(Long crewId, String uuid) {
		return chatLastStatusRepository.findLastReadAtByCrewIdAndUuid(crewId, uuid)
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastReadAt()))
			.map(ChatDTO::toDto);
	}

	public Flux<LastChatDTO> getLatestChatAndStream(Long crewId, String uuid) {
		return chatJoinTimeRepository.findByUuidAndCrewId(uuid, crewId)
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_EXIST_JOIN_TIME)))
			.flatMapMany(joinTime -> chatLastStatusRepository.findByCrewIdAndUuid(crewId, uuid)
				.switchIfEmpty(
					Mono.error(new GlobalException(ErrorStatus.NO_EXIST_CHAT_UNREAD_STATUS)))
				.flatMapMany(
					lastStatus -> chatRepository.findLatestByCrewId(crewId, joinTime.getJoinTime())
						.switchIfEmpty(newCreateCrewChat(crewId, uuid))
						.flatMap(latestChat -> setLastChatContent(latestChat)
							.flatMap(
								lastChatContent -> chatRepository.countByCrewIdAndCreatedAtBetween(
										crewId, lastStatus.getLastReadAt(), latestChat.getCreatedAt())
									.map(unreadCount -> LastChatDTO.toDTO(lastChatContent,
										Math.min(999, unreadCount.intValue()),
										latestChat.getCreatedAt()))
							)
						)
						.flatMapMany(lastChatInfoDTO -> {
							Flux<LastChatDTO> firstLastChat = Flux.just(lastChatInfoDTO);
							Flux<Chat> streamLastChat = chatRepository.findByCrewIdAndCreatedAtAfter(
								crewId, lastChatInfoDTO.getCreatedAt());
							return firstLastChat.concatWith(
								streamLastChat.flatMap(chat -> setLastChatContent(chat)
									.map(content -> LastChatDTO.toDTO(content, null,
										chat.getCreatedAt()))
								));
						})
				)
			);
	}

	private Mono<Chat> newCreateCrewChat(Long crewId, String uuid) {
		return chatRepository.findFirstByCrewIdAndUuid(crewId, uuid)
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_LAST_CHAT)))
			.map(firstChat -> Chat.builder()
				.text("새로운 소모임 채팅방이 생성되었습니다.")
				.crewId(crewId)
				.uuid(uuid)
				.createdAt(firstChat.getCreatedAt())
				.build());
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
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_FIND_CHAT_UNREAD_STATUS)))
			.flatMap(chatUnReadStatus -> chatLastStatusRepository
				.save(lastStatusModifyDTO.toEntity(chatUnReadStatus)))
			.onErrorMap(e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
			.then();
	}

	@Override
	public Mono<Void> createEntryChatAndJoinTime(ChatEntryExitDTO entryExitDTO) {
		return chatRepository.save(entryExitDTO.toChatEntity())
			.flatMap(chat -> chatJoinTimeRepository.save(
				entryExitDTO.toChatJoinTimeEntity(chat.getCreatedAt())))
			.then()
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Mono<Void> createExitChatAndDeleteJoinTime(ChatEntryExitDTO entryExitDTO) {
		return chatRepository.save(entryExitDTO.toChatEntity())
			.flatMap(chat -> chatJoinTimeRepository.findByUuidAndCrewId(entryExitDTO.getUuid(),
					entryExitDTO.getCrewId())
				.flatMap(chatJoinTimeRepository::delete))
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}
}
