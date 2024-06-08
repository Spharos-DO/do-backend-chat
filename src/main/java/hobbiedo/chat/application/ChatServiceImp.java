package hobbiedo.chat.application;

import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastChatTimeDTO;
import hobbiedo.chat.dto.response.ChatStreamDTO;
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
			.onErrorMap(e -> new GlobalException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@Override
	public Flux<ChatStreamDTO> getStreamChat(Long crewId, String uuid) {
		return chatLastStatusRepository.findLastReadAtByCrewIdAndUuid(crewId, uuid)
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findChatByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastReadAt()))
			.map(ChatStreamDTO::toDto);
	}

	// @Override
	// public Flux<LastChatInfoDTO> getLatestChats(String uuid) {
	// 	return chatUnReadStatusRepository.findByUuid(uuid)
	// 		.map(ChatUnReadStatus::getCrewId)
	// 		.flatMap(crewId -> chatRepository.findLatestByCrewId(crewId)
	// 			.flatMapMany(latestChat -> {
	// 				Instant latestCreatedAt = latestChat.getCreatedAt();
	// 				return chatRepository.findChatByCrewIdAndCreatedAtOrAfter(crewId,
	// 						latestCreatedAt)
	// 					.flatMap(chat -> setLastChatContent(chat)  // 비동기적으로 내용 처리
	// 						.map(lastChatContent -> LastChatInfoDTO.toDTO(chat, lastChatContent)));
	// 			})
	// 		);
	// }
	// public Flux<LastChatInfoDTO> streamLatestChats(String uuid) {
	// 	return chatRepository.findLatestChatsByUuid(uuid)
	// 		.collectList()  // 각 크루의 최신 채팅 시간들
	// 		.flatMapMany(latestChats -> {
	// 			// 각 크루의 최신 채팅 시간 기준으로 정렬
	// 			List<Chat> sortedChats = latestChats.stream()
	// 				.sorted(Comparator.comparing(Chat::getCreatedAt))
	// 				.toList();
	//
	// 			// 정렬된 최신 채팅 데이터를 Flux로 변환
	// 			Flux<Chat> sortedChatsFlux = Flux.fromIterable(sortedChats);
	//
	// 			// 각 크루의 최신 채팅 시간 이후의 메시지를 스트리밍
	// 			Flux<Chat> newChatsFlux = Flux.fromIterable(sortedChats)
	// 				.flatMap(chat -> chatRepository.findChatByCrewIdAndCreatedAtOrAfter(
	// 				chat.getCrewId(), chat.getCreatedAt()));
	//
	// 			return newChatsFlux.flatMap(chat -> setLastChatContent(chat)
	// 				.map(lastChatContent -> LastChatInfoDTO.toDTO(chat, lastChatContent)));
	// 		});
	// }

	// private Mono<String> setLastChatContent(Chat chat) {
	// 	return Mono.justOrEmpty(chat.getText())
	// 		.switchIfEmpty(
	// 			Mono.justOrEmpty(chat.getImageUrl())
	// 				.map(url -> "사진을 보냈습니다.")
	// 				.switchIfEmpty(Mono.error(new GlobalException(ErrorStatus.NO_CHAT_CONTENT)))
	// 		);
	// }

	@Override
	public Mono<Void> updateLastReadAt(String uuid, Long crewId,
		LastChatTimeDTO lastChatTimeDTO) { // 안 읽은 채팅 개수 수정 같이 필요
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
