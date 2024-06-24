package hobbiedo.chat.kafka.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import hobbiedo.chat.application.mvc.ChatService;
import hobbiedo.chat.application.reactive.ReactiveChatService;
import hobbiedo.chat.kafka.dto.ChatEntryExitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final ChatService chatService;
	private final ReactiveChatService reactiveChatService;

	// 소모임 생성 이벤트 수신 - ChatLastStatus 생성
	@KafkaListener(topics = {"create-crew-topic",
		"join-crew-topic"}, groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "crewEntryExitKafkaListenerContainerFactory")
	public void listenToScoreAddTopic(ChatEntryExitDTO eventDto) {
		reactiveChatService.createEntryChatAndJoinTime(eventDto)
			.doOnSuccess(chat -> chatService.createChatStatus(eventDto))
			.subscribe();
	}

	// 소모임 회원 입퇴장 채팅 알림 전송
	@KafkaListener(topics = {"exit-crew-topic",
		"force-exit-crew-topic"}, groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "crewEntryExitKafkaListenerContainerFactory")
	public void listenToSendEntryExitChatTopic(ChatEntryExitDTO eventDto) {
		chatService.deleteChatStatus(eventDto);
		reactiveChatService.createExitChatAndDeleteJoinTime(eventDto).subscribe();
	}
}
