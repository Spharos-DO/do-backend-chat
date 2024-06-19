package hobbiedo.chat.kafka.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import hobbiedo.chat.application.mvc.ChatService;
import hobbiedo.chat.application.reactive.ReactiveChatService;
import hobbiedo.chat.kafka.dto.ChatLastStatusCreateDTO;
import hobbiedo.chat.kafka.dto.EntryExitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final ChatService chatService;
	private final ReactiveChatService reactiveChatService;

	// 소모임 생성 이벤트 수신 - ChatLastStatus 생성
	@KafkaListener(topics = "create-chat-status-topic", groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "createLastStatusKafkaListenerContainerFactory")
	public void listenToScoreAddTopic(ChatLastStatusCreateDTO eventDto) {
		chatService.createChatStatus(eventDto);
	}

	// 소모임 회원 입퇴장 채팅 알림 전송
	@KafkaListener(topics = "entry-exit-chat-topic", groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "sendEntryExitChatKafkaListenerContainerFactory")
	public void listenToSendEntryExitChatTopic(EntryExitDTO eventDto) {
		reactiveChatService.sendEntryExitChat(eventDto).subscribe();
	}
}
