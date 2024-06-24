package hobbiedo.chat.kafka.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import hobbiedo.chat.application.mvc.ChatService;
import hobbiedo.chat.application.reactive.ReactiveChatService;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final ChatService chatService;
	private final ReactiveChatService reactiveChatService;

	// 소모임 생성, 가입 이벤트 수신 - ChatLastStatus 생성, 채팅 입장 알림 전송
	@KafkaListener(topics = {"create-crew-topic",
		"join-crew-topic"}, groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "crewEntryExitKafkaListenerContainerFactory")
	public void listenToScoreAddTopic(CrewEntryExitDTO eventDto) {
		chatService.createChatStatus(eventDto);
		reactiveChatService.createUnreadCount(eventDto).subscribe();
		reactiveChatService.sendEntryExitChat(eventDto).subscribe();
	}

	// 소모임 회원 탈퇴, 강퇴 이벤트 - ChatLastStatus 삭제, 채팅 퇴장 알림 전송
	@KafkaListener(topics = {"exit-crew-topic",
		"force-exit-crew-topic"}, groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "crewEntryExitKafkaListenerContainerFactory")
	public void listenToSendEntryExitChatTopic(CrewEntryExitDTO eventDto) {
		chatService.deleteChatStatus(eventDto);
		reactiveChatService.deleteUnreadCount(eventDto).subscribe();
		reactiveChatService.sendEntryExitChat(eventDto).subscribe();
	}
}
