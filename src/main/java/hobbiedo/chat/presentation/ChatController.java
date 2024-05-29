package hobbiedo.chat.presentation;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
@Tag(name = "채팅", description = "Chat API")
@RequestMapping("/v1/users/chat")
public class ChatController {
	private final ChatService chatService;

	@Operation(summary = "채팅 전송", description = "소모임에 하나의 채팅을 전송한다.")
	@PostMapping
	public Mono<ApiResponse<Chat>> sendChat(@RequestBody ChatSendVo chatSendVo,
		@RequestHeader String uuid) {
		return chatService.sendChat(chatSendVo, uuid)
			.map(chat -> ApiResponse.onSuccess(SuccessStatus.CREATE_CHAT, chat));
	}

	@Operation(summary = "(특정 소모임의) 실시간 채팅 내역 조회", description = "특정 시간(Instant)과 그 이후의 채팅 내역을 실시간으로 조회한다.")
	@GetMapping(value = "/{crewId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ApiResponse<Chat>> getChatByRoomId(@PathVariable Long crewId,
		@RequestHeader String uuid) {
		return chatService.getChatByCrewIdAfterDateTime(crewId, uuid)
			.map(chat -> ApiResponse.onSuccess(SuccessStatus.FIND_CHAT_CONTENT, chat))
			.subscribeOn(Schedulers.boundedElastic());
	}
}
