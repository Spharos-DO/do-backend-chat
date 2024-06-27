package hobbiedo.chat.application.reactive;

import java.util.List;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.domain.ChatLastStatus;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatDTO;
import hobbiedo.chat.dto.response.LastChatDTO;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatJoinTimeRepository;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatLastStatusRepository;
import hobbiedo.chat.infrastructure.reactive.ReactiveChatRepository;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;
import hobbiedo.global.exception.GlobalException;
import hobbiedo.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReactiveChatServiceImp implements ReactiveChatService {
	private static final String CREATE_NEW_CHAT = "새로운 채팅이 생성되었습니다.";
	private final ReactiveChatRepository chatRepository;
	private final ReactiveChatLastStatusRepository chatLastStatusRepository;
	private final ReactiveChatJoinTimeRepository chatJoinTimeRepository;
	private final UnreadCountService unreadCountService;
	private final RedisPubSubService redisPubSubService;

	@Override
	public Mono<Void> sendChat(ChatSendDTO chatSendDTO, String uuid) {
		return chatLastStatusRepository.findByCrewIdAndConnectionStatusFalse(
				chatSendDTO.getCrewId())
			.collectList()
			.flatMap(chatLastStatusList -> {
				Mono<Void> updateUnreadCount = updateUnreadCount(chatSendDTO, chatLastStatusList);
				Mono<Void> saveMessage = saveMessageAndRollback(chatSendDTO, uuid,
					chatLastStatusList);
				Mono<Void> publishMessage = Mono.empty();
				if (chatSendDTO.getEntryExitNotice() == null) {
					publishMessage = redisPubSubService.publish(
						new ChannelTopic("" + chatSendDTO.getCrewId()), CREATE_NEW_CHAT);
				}

				return updateUnreadCount
					.then(saveMessage)
					.then(publishMessage)
					.then();
			});
	}

	private Mono<Void> updateUnreadCount(ChatSendDTO chatSendDTO,
		List<ChatLastStatus> chatLastStatusList) {
		return Flux.fromIterable(chatLastStatusList)
			.flatMap(chatLastStatus -> {
				// entryExitNotice가 null인 경우에만 처리
				if (chatSendDTO.getEntryExitNotice() == null) {
					return unreadCountService.getUnreadCount(chatSendDTO.getCrewId(),
							chatLastStatus.getUuid())
						.filter(unreadCount -> unreadCount < 999) // 999 미만일 때만 처리
						.flatMap(unreadCount -> unreadCountService.incrementUnreadCount(
							chatSendDTO.getCrewId(), chatLastStatus.getUuid()));
				} else {
					return Mono.empty(); // entryExitNotice가 null이 아닌 경우에는 아무 작업도 하지 않음
				}
			})
			.then()
			.onErrorMap(
				e1 -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e1.getMessage()));
	}

	private Mono<Void> saveMessageAndRollback(ChatSendDTO chatSendDTO, String uuid,
		List<ChatLastStatus> chatLastStatusList) {
		return chatRepository.save(chatSendDTO.toEntity(uuid))
			.onErrorResume(e2 -> {
				// 메시지 저장 실패 시 Redis 롤백
				return Flux.fromIterable(chatLastStatusList)
					.flatMap(chatLastStatus -> {
						// entryExitNotice가 null인 경우에만 처리
						if (chatSendDTO.getEntryExitNotice() == null) {
							return unreadCountService.getUnreadCount(chatSendDTO.getCrewId(),
									chatLastStatus.getUuid())
								.filter(unreadCount -> unreadCount < 999) // 999 미만일 때만 처리
								.flatMap(unreadCount -> unreadCountService.decrementUnreadCount(
									chatSendDTO.getCrewId(), chatLastStatus.getUuid()));
						} else {
							return Mono.empty(); // entryExitNotice가 null이 아닌 경우에는 아무 작업도 하지 않음
						}
					})
					.then()
					.onErrorMap(e3 -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR,
						e3.getMessage()))
					.then(Mono.error(
						new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e2.getMessage())));
			})
			.then();
	}

	@Override
	public Flux<ChatDTO> getStreamChat(Long crewId, String uuid) {
		return chatLastStatusRepository.findLastReadAtByCrewIdAndUuid(crewId, uuid)
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastReadAt()))
			.map(ChatDTO::toDto);
	}

	@Override
	public Flux<String> subscribeToChannelByUuid(String uuid) {
		return chatLastStatusRepository.findCrewIdsByUuid(uuid)
			.flatMap(chatLastStatus -> redisPubSubService.subscribeToChannel(
				String.valueOf(chatLastStatus.getCrewId())));
	}

	@Override
	public Flux<LastChatDTO> getLatestChatAndStream(Long crewId, String uuid) {
		return chatJoinTimeRepository.findByUuidAndCrewId(uuid, crewId)
			.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_EXIST_JOIN_TIME)))
			.flatMapMany(joinTime ->
				chatRepository.findLatestByCrewId(crewId, joinTime.getJoinTime())
					.switchIfEmpty(newCreateCrewChat(crewId, uuid))
					.flatMapMany(latestChat ->
						chatRepository.findByCrewIdAndCreatedAtAfter(crewId,
								latestChat.getCreatedAt())
							.flatMap(chat ->
								setLastChatContent(latestChat)
									.flatMap(lastChatContent ->
										unreadCountService.getUnreadCount(crewId, uuid)
											.map(unreadCount ->
												LastChatDTO.toDTO(lastChatContent,
													unreadCount.intValue(),
													latestChat.getCreatedAt())
											)
									)
							)
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
			.flatMap(chatUnReadStatus ->
				chatLastStatusRepository.save(lastStatusModifyDTO.toEntity(chatUnReadStatus))
					.flatMap(savedStatus -> {
						if (savedStatus.isConnectionStatus()) {
							return unreadCountService.initializeUnreadCount(
									lastStatusModifyDTO.getCrewId(),
									uuid, 0)
								.then(); // initializeUnreadCount 끝날때까지 기다리지 않고 비동기로 처리
						} else {
							return Mono.empty();
						}
					})
			)
			.onErrorMap(e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
			.then();
	}

	@Override
	public Mono<Void> createEntryChatAndJoinTime(CrewEntryExitDTO entryExitDTO) {
		return chatRepository.save(entryExitDTO.toChatEntity())
			.flatMap(chat -> chatJoinTimeRepository.save(
				entryExitDTO.toChatJoinTimeEntity(chat.getCreatedAt())))
			.then()
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Mono<Void> createExitChatAndDeleteJoinTime(CrewEntryExitDTO entryExitDTO) {
		return chatRepository.save(entryExitDTO.toChatEntity())
			.flatMap(chat -> chatJoinTimeRepository.findByUuidAndCrewId(entryExitDTO.getUuid(),
					entryExitDTO.getCrewId())
				.flatMap(chatJoinTimeRepository::delete))
			.onErrorMap(
				e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Mono<Void> deleteUnreadCount(CrewEntryExitDTO entryExitDTO) {
		return unreadCountService.deleteUnreadCount(entryExitDTO.getCrewId(),
			entryExitDTO.getUuid());
	}
	// public Flux<LastChatDTO> getLatestChatAndStream(Long crewId, String uuid) {
	// 	return chatJoinTimeRepository.findByUuidAndCrewId(uuid, crewId)
	// 		.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_EXIST_JOIN_TIME)))
	// 		.flatMapMany(joinTime -> chatLastStatusRepository.findByCrewIdAndUuid(crewId, uuid)
	// 			.switchIfEmpty(
	// 				Mono.error(new GlobalException(ErrorStatus.NO_EXIST_CHAT_UNREAD_STATUS)))
	// 			.flatMapMany(
	// 				lastStatus -> chatRepository.findLatestByCrewId(crewId, joinTime.getJoinTime())
	// 					.switchIfEmpty(newCreateCrewChat(crewId, uuid))
	// 					.flatMap(latestChat -> setLastChatContent(latestChat)
	// 						.flatMap(
	// 							lastChatContent -> chatRepository.countByCrewIdAndCreatedAtBetween(
	// 									crewId, lastStatus.getLastReadAt(), latestChat.getCreatedAt())
	// 								.map(unreadCount -> LastChatDTO.toDTO(lastChatContent,
	// 									Math.min(999, unreadCount.intValue()),
	// 									latestChat.getCreatedAt()))
	// 						)
	// 					)
	// 					.flatMapMany(lastChatInfoDTO -> {
	// 						Flux<LastChatDTO> firstLastChat = Flux.just(lastChatInfoDTO);
	// 						Flux<Chat> streamLastChat = chatRepository.findByCrewIdAndCreatedAtAfter(
	// 							crewId, lastChatInfoDTO.getCreatedAt());
	// 						return firstLastChat.concatWith(
	// 							streamLastChat.flatMap(chat -> setLastChatContent(chat)
	// 								.map(content -> LastChatDTO.toDTO(content, null,
	// 									chat.getCreatedAt()))
	// 							));
	// 					})
	// 			)
	// 		);
	// }
}
