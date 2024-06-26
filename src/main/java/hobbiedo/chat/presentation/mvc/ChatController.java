package hobbiedo.chat.presentation.mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hobbiedo.chat.application.mvc.ChatService;
import hobbiedo.global.base.BaseResponse;
import hobbiedo.global.exception.GlobalException;
import hobbiedo.global.exception.GlobalExceptionHandler;
import hobbiedo.global.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users/chat")
@RequiredArgsConstructor
@Tag(name = "채팅", description = "Chat API")
public class ChatController {
	private final ChatService chatService;
	private final GlobalExceptionHandler globalExceptionHandler;

	@Operation(summary = "(특정 소모임의) 이전 채팅 내역 조회", description = "페이지에 따른 10개의 이전 채팅 내역을 조회한다.")
	@GetMapping("/history/{crewId}")
	public BaseResponse<?> getChatHistoryBefore(@PathVariable Long crewId,
		@RequestParam int page, @RequestHeader(name = "Uuid") String uuid) {
		try {
			return BaseResponse.onSuccess(SuccessStatus.FIND_CHAT_HISTORY,
				chatService.getChatHistoryBefore(crewId, uuid, page));
		} catch (GlobalException ex) {
			return globalExceptionHandler.baseError(ex);
		} catch (Exception ex) {
			return globalExceptionHandler.exceptionError(ex);
		}
	}

	@Operation(summary = "한 회원의 채팅방 리스트 전체 조회",
		description = "한 유저에 해당하는 전체 소모임 리스트의 마지막 채팅과 안읽음 개수를 조회한다.")
	@GetMapping("/latest/list")
	public BaseResponse<?> getLatestChatList(
		@RequestHeader(name = "Uuid") String uuid) {
		try {
			return BaseResponse.onSuccess(SuccessStatus.FIND_CHAT_LIST,
				chatService.getChatList(uuid));
		} catch (GlobalException ex) {
			return globalExceptionHandler.baseError(ex);
		} catch (Exception ex) {
			return globalExceptionHandler.exceptionError(ex);
		}
	}

	@Operation(summary = "(특정 소모임) 사진 모아보기", description = "특정 소모임의 이미지 URL이 있는 채팅 내역을 조회한다.")
	@GetMapping("/image/{crewId}")
	public BaseResponse<?> getChatsWithImageUrl(@PathVariable Long crewId) {
		try {
			return BaseResponse.onSuccess(SuccessStatus.FIND_IMAGE_CHAT,
				chatService.getChatsWithImageUrl(crewId));
		} catch (GlobalException ex) {
			return globalExceptionHandler.baseError(ex);
		} catch (Exception ex) {
			return globalExceptionHandler.exceptionError(ex);
		}

	}
}
