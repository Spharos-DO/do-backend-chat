package hobbiedo.chat.presentation.mvc;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hobbiedo.chat.application.mvc.ChatService;
import hobbiedo.chat.dto.response.ChatHistoryDTO;
import hobbiedo.chat.dto.response.ChatImageListDTO;
import hobbiedo.global.base.BaseResponse;
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

	@Operation(summary = "(특정 소모임의) 이전 채팅 내역 조회", description = "페이지에 따른 10개의 이전 채팅 내역을 조회한다.")
	@GetMapping("/history/{crewId}")
	public BaseResponse<ChatHistoryDTO> getChatHistoryBefore(@PathVariable Long crewId,
		@RequestParam int page, @RequestHeader(name = "Uuid") String uuid) {
		return BaseResponse.onSuccess(SuccessStatus.FIND_CHAT_HISTORY,
			chatService.getChatHistoryBefore(crewId, uuid, page));
	}

	@Operation(summary = "(특정 소모임) 사진 모아보기", description = "특정 소모임의 이미지 URL이 있는 채팅 내역을 조회한다.")
	@GetMapping("/image/{crewId}")
	public BaseResponse<List<ChatImageListDTO>> getChatsWithImageUrl(@PathVariable Long crewId) {
		return BaseResponse.onSuccess(SuccessStatus.FIND_IMAGE_CHAT,
			chatService.getChatsWithImageUrl(crewId));
	}

	// @Operation(summary = "(채팅방 리스트에서) 마지막 채팅과 안읽음 개수 리스트 조회 (처음)",
	// 	description = "처음 소모임 리스트를 띄울 때, 한 유저에 해당하는 전체 소모임 리스트의 마지막 채팅과 안읽음 개수를 조회한다.")
	// @GetMapping("/latest/list")
	// public BaseResponse<List<LastChatDTO>> getLatestChatList(
	// 	@RequestHeader(name = "Uuid") String uuid) {
	// 	return BaseResponse.onSuccess(SuccessStatus.FIND_LAST_CHAT_LIST,
	// 		chatService.getChatList(uuid));
	// }
}
