package hobbiedo.chat.presentation;

import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hobbiedo.chat.application.ChatService;
import hobbiedo.chat.dto.request.ChatSendDTO;
import hobbiedo.chat.dto.request.LastStatusModifyDTO;
import hobbiedo.chat.dto.response.ChatStreamDTO;
import hobbiedo.chat.dto.response.LastChatInfoDTO;
import hobbiedo.global.base.BaseResponse;
import hobbiedo.global.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@Tag(name = "채팅", description = "Chat API")
@RequestMapping("/v1/users/chat")
public class ChatController {
	private final ChatService chatService;

	@Operation(summary = "채팅 전송", description = "소모임에 하나의 채팅을 전송한다.")
	@PostMapping
	public Mono<BaseResponse<Void>> sendChat(@RequestBody ChatSendDTO chatSendDTO,
		@RequestHeader(name = "Uuid") String uuid) {
		return chatService.sendChat(chatSendDTO, uuid)
			.then(Mono.just(BaseResponse.onSuccess(SuccessStatus.CREATE_CHAT, null)));
	}

	@Operation(summary = "(특정 소모임의) 실시간 채팅 내역 조회", description = "특정 시간(Instant)과 그 이후의 채팅 내역을 실시간으로 조회한다.")
	@GetMapping(value = "/{crewId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<BaseResponse<ChatStreamDTO>> getStreamChat(@PathVariable Long crewId,
		@RequestHeader(name = "Uuid") String uuid) {
		return chatService.getStreamChat(crewId, uuid)
			.map(chatStreamDTO -> BaseResponse.onSuccess(SuccessStatus.FIND_CHAT_CONTENT,
				chatStreamDTO));
		//	.subscribeOn(Schedulers.boundedElastic());
	}

	@Operation(summary = "(채팅방 리스트에서) 실시간 마지막 채팅과 안읽음 개수 조회", description = "채팅방 리스트에서 채팅방당 실시간으로 업데이트 되는 내역을 조회한다.")
	@GetMapping(value = "/latest/stream/{crewId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<BaseResponse<LastChatInfoDTO>> getLatestChats(@PathVariable Long crewId,
		@RequestParam Instant lastChatAt) {
		return chatService.getStreamLatestChat(crewId, lastChatAt)
			.map(lastChatInfoDTO -> BaseResponse.onSuccess(SuccessStatus.FIND_LAST_CHAT,
				lastChatInfoDTO));
		//.subscribeOn(Schedulers.boundedElastic());
	}

	@Operation(summary = "채팅방 접속 여부 변경",
		description = "한 유저가 채팅방을 접속할 때와 나갈 때, 접속 여부를 변경한다.")
	@PutMapping("/connection")
	public Mono<BaseResponse<Void>> updateLastReadAt(
		@RequestBody LastStatusModifyDTO lastStatusModifyDTO,
		@RequestHeader(name = "Uuid") String uuid) {
		return chatService.updateLastStatusAt(lastStatusModifyDTO, uuid)
			.then(Mono.just(BaseResponse.onSuccess(SuccessStatus.UPDATE_CONNECTION_STATUS, null)));
	}

	// 	@Operation(summary = "(한 유저의 특정 소모임에서) 안 읽은 채팅 개수 조회",
	// 		description = "채팅방 리스트에서 한 유저가 특정 소모임에 대한 안읽은 채팅 메시지의 개수를 조회한다.")
	// 	@GetMapping("/unread-count/{crewId}")
	// 	public Mono<BaseResponse<UnReadCountDTO>> getUnreadCount(@PathVariable Long crewId,
	// 		@RequestHeader String uuid) {
	// 		return chatService.getUnreadCount(crewId, uuid)
	// 			.map(unReadCountDTO -> BaseResponse.onSuccess(SuccessStatus.FIND_UNREAD_COUNT,
	// 				unReadCountDTO));
	// 	}
}
