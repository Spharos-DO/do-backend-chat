package hobbiedo.chat.application.mvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hobbiedo.chat.application.reactive.UnreadCountService;
import hobbiedo.chat.domain.Chat;
import hobbiedo.chat.domain.ChatLastStatus;
import hobbiedo.chat.dto.response.ChatDTO;
import hobbiedo.chat.dto.response.ChatHistoryDTO;
import hobbiedo.chat.dto.response.ChatImageDTO;
import hobbiedo.chat.dto.response.ChatImageListDTO;
import hobbiedo.chat.dto.response.ChatListDTO;
import hobbiedo.chat.dto.response.LastChatListDTO;
import hobbiedo.chat.infrastructure.mvc.ChatJoinTimeRepository;
import hobbiedo.chat.infrastructure.mvc.ChatLastStatusRepository;
import hobbiedo.chat.infrastructure.mvc.ChatRepository;
import hobbiedo.chat.kafka.dto.CrewEntryExitDTO;
import hobbiedo.global.exception.GlobalException;
import hobbiedo.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImp implements ChatService {
	private final ChatRepository chatRepository;
	private final ChatLastStatusRepository chatStatusRepository;
	private final ChatJoinTimeRepository chatJoinTimeRepository;
	private final UnreadCountService unreadCountService;

	@Override
	public ChatHistoryDTO getChatHistoryBefore(Long crewId, String uuid, int page) {
		int size = 10; // 페이지 당 데이터 개수
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
		Instant lastReadAt = getLastReadAt(uuid, crewId);
		Instant joinTime = chatJoinTimeRepository.findJoinTimeByUuidAndCrewId(uuid, crewId)
			.orElseThrow(() -> new GlobalException(ErrorStatus.NO_EXIST_JOIN_TIME))
			.getJoinTime();

		Page<Chat> chatPage = chatRepository.findLastChatByCrewId(crewId, joinTime, lastReadAt,
			pageable);
		if (chatPage.isEmpty()) {
			throw new GlobalException(ErrorStatus.NO_EXIST_CHAT);
		}

		List<ChatListDTO> chatListDtos = chatPage.getContent().stream()
			.collect(Collectors.groupingBy(
				chat -> LocalDate.ofInstant(chat.getCreatedAt(), ZoneId.systemDefault())))
			.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(entry -> ChatListDTO.toDto(
				entry.getKey(),
				entry.getValue().stream()
					.sorted(Comparator.comparing(Chat::getCreatedAt))
					.map(ChatDTO::toDto)
					.toList()))
			.toList();

		return ChatHistoryDTO.toDto(chatPage.getTotalPages() - 1, chatListDtos);
	}

	@Override
	public List<LastChatListDTO> getChatList(String uuid) {
		List<ChatLastStatus> crewIdList = chatStatusRepository.findByUuid(uuid);
		if (crewIdList.isEmpty()) {
			throw new GlobalException(ErrorStatus.NO_EXIST_CHAT_LIST);
		}

		return crewIdList.stream()
			.map(chatStatus -> {
				Long crewId = chatStatus.getCrewId();
				Instant joinTime = chatJoinTimeRepository.findJoinTimeByUuidAndCrewId(uuid, crewId)
					.orElseThrow(() -> new GlobalException(ErrorStatus.NO_EXIST_JOIN_TIME))
					.getJoinTime();
				// 채팅방의 마지막 메시지 조회
				Chat lastChat = chatRepository.findLastChatByCrewId(crewId, joinTime)
					.orElse(newCreateCrewChat(crewId, uuid));
				String content = lastChat.getImageUrl() != null ? "사진을 보냈습니다." : lastChat.getText();
				// 안 읽은 메시지 개수 조회
				Long countLong = unreadCountService.getUnreadCount(crewId, uuid).block();
				int count = countLong != null ? Math.toIntExact(countLong) : 0;

				return LastChatListDTO.toDto(lastChat, content, count);
			})
			.sorted(Comparator.comparing(LastChatListDTO::getCreatedAt).reversed())
			.toList();
	}

	private Chat newCreateCrewChat(Long crewId, String uuid) {
		Chat chat = chatRepository.findFirstByCrewIdAndUuid(crewId, uuid)
			.orElseThrow(() -> new GlobalException(ErrorStatus.NO_FIND_LAST_CHAT));
		return Chat.builder()
			.text("새로운 소모임 채팅방이 생성되었습니다.")
			.crewId(crewId)
			.uuid(uuid)
			.createdAt(chat.getCreatedAt())
			.build();
	}

	private Instant getLastReadAt(String uuid, Long crewId) {
		return chatStatusRepository.findByUuidAndCrewId(crewId, uuid)
			.map(ChatLastStatus::getLastReadAt)
			.orElseThrow(() -> new GlobalException(ErrorStatus.NO_EXIST_CHAT_UNREAD_STATUS));
	}

	@Override
	public List<ChatImageListDTO> getChatsWithImageUrl(Long crewId) {
		List<Chat> chatList = chatRepository.findByCrewIdAndImageUrl(crewId);
		if (chatList.isEmpty()) {
			throw new GlobalException(ErrorStatus.NO_EXIST_IMAGE_CHAT);
		}

		return chatList.stream()
			.collect(Collectors.groupingBy(
				chat -> LocalDate.ofInstant(chat.getCreatedAt(), ZoneId.systemDefault())))
			.entrySet().stream()
			.sorted(Map.Entry.<LocalDate, List<Chat>>comparingByKey().reversed())
			.map(entry -> ChatImageListDTO.toDto(entry.getKey(),
				entry.getValue().stream()
					.map(ChatImageDTO::toDto)
					.sorted(Comparator.comparing(ChatImageDTO::getCreatedAt).reversed())
					.toList()))
			.toList();
	}

	@Transactional
	@Override
	public void createChatStatus(CrewEntryExitDTO chatEntryExitDTO) {
		chatStatusRepository.save(chatEntryExitDTO.toChatLastStatusEntity());
	}

	@Transactional
	@Override
	public void deleteChatStatus(CrewEntryExitDTO chatEntryExitDTO) {
		chatStatusRepository.deleteByCrewIdAndUuid(chatEntryExitDTO.getCrewId(),
			chatEntryExitDTO.getUuid());
	}

	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
	@Transactional
	@Override
	public void deleteOldChatsWithImageUrl() {
		// 3개월 전 시각 계산
		LocalDateTime expiryDateTime = LocalDateTime.now().minusMonths(3);

		// LocalDateTime을 Instant로 변환
		Instant expiryDate = expiryDateTime.atZone(ZoneId.systemDefault()).toInstant();
		chatRepository.deleteByImageUrlExistsAndCreatedAtBefore(expiryDate);
	}
}
