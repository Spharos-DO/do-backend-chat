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
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.global.base.BaseResponse;
import hobbiedo.global.status.SuccessStatus;
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
	public Mono<BaseResponse<Chat>> sendChat(@RequestBody ChatSendDTO chatSendVo,
		@RequestHeader String uuid) {
		return chatService.sendChat(chatSendVo, uuid)
			.map(chat -> BaseResponse.onSuccess(SuccessStatus.CREATE_CHAT, chat));
	}

	@Operation(summary = "(특정 소모임의) 실시간 채팅 내역 조회", description = "특정 시간(Instant)과 그 이후의 채팅 내역을 실시간으로 조회한다.")
	@GetMapping(value = "/{crewId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<BaseResponse<Chat>> getStreamChat(@PathVariable Long crewId,
		@RequestHeader String uuid) {
		return chatService.getStreamChat(crewId, uuid)
			.map(chat -> BaseResponse.onSuccess(SuccessStatus.FIND_CHAT_CONTENT, chat))
			.subscribeOn(Schedulers.boundedElastic());
	}

	// @Operation(summary = "(채팅 리스트에서) 마지막 채팅 정보 조회", description = "한 유저의 채팅방 리스트에서 채팅방들의 마지막 대화 관련 정보를 조회한다. ")
	// @GetMapping(value = "/latest", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	// public Flux<BaseResponse<LastChatInfoDTO>> getLatestChats(@RequestHeader String uuid) {
	// 	return chatService.getLatestChats(uuid)
	// 		.map(lastChatInfoDTO -> BaseResponse.onSuccess(SuccessStatus.FIND_LAST_CHAT, lastChatInfoDTO))
	// 		.subscribeOn(Schedulers.boundedElastic());
	// }
}
