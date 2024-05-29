package hobbiedo.chat.application;

import java.time.Instant;

import org.springframework.stereotype.Service;

import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.infrastructure.ChatRepository;
import hobbiedo.chat.infrastructure.ChatUnReadStatusRepository;
import hobbiedo.chat.vo.request.ChatSendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatServiceImp implements ChatService {
	private final ChatRepository chatRepository;
	private final ChatUnReadStatusRepository chatUnReadStatusRepository;

	@Override
	public Mono<Chat> sendChat(ChatSendVo chatSendVo,
		String uuid) { // 응답을 void 로 보내면 error를 잡을 수 없어서 chat으로 return
		return chatRepository.save(Chat.builder()
			.crewId(chatSendVo.getCrewId())
			.uuid(uuid)
			.text(chatSendVo.getText())
			.imageUrl(chatSendVo.getImageUrl())
			.videoUrl(chatSendVo.getVideoUrl())
			.entryExitNotice(chatSendVo.getEntryExitNotice())
			.createdAt(Instant.now())
			.build());
	}

	@Override
	public Flux<Chat> getChatByCrewIdAfterDateTime(Long crewId, String uuid) {
		return chatUnReadStatusRepository.findByUuidAndCrewId(uuid, crewId)
			.flatMapMany(
				chatUnReadStatus -> chatRepository.findChatByCrewIdAndCreatedAtOrAfter(crewId,
					chatUnReadStatus.getLastAt()));
	}
}
