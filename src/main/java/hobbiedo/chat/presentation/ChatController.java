package hobbiedo.chat.presentation;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hobbiedo.chat.application.ChatService;
import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.vo.request.ChatSendVo;
import hobbiedo.global.base.ApiResponse;
import hobbiedo.global.base.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users/chat")
@Slf4j
public class ChatController {
	private final ChatService chatService;

	@PostMapping
	public Mono<ApiResponse<Chat>> sendChat(@RequestBody ChatSendVo chatSendVo,
		@RequestHeader String uuid) {
		return chatService.sendChat(chatSendVo, uuid)
			.map(chat -> ApiResponse.onSuccess(SuccessStatus.CREATE_CHAT, chat));
	}

	@GetMapping(value = "/{crewId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ApiResponse<Chat>> getChatByRoomId(@PathVariable String crewId) {
		LocalDateTime since = LocalDateTime.now();
		return chatService.getChatByCrewIdAfterDateTime(crewId, since)
			.map(chat -> ApiResponse.onSuccess(SuccessStatus.FIND_CHAT_CONTENT, chat))
			.subscribeOn(Schedulers.boundedElastic());
	}
}
