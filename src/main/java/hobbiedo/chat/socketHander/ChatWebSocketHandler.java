// package hobbiedo.chat.socketHander;
//
// import java.util.Map;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.socket.WebSocketHandler;
// import org.springframework.web.reactive.socket.WebSocketMessage;
// import org.springframework.web.reactive.socket.WebSocketSession;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import hobbiedo.chat.application.ChatService;
// import hobbiedo.chat.dto.request.ChatSendDTO;
// import hobbiedo.chat.dto.request.LastChatTimeDTO;
// import hobbiedo.global.base.BaseResponse;
// import hobbiedo.global.exception.GlobalException;
// import hobbiedo.global.status.ErrorStatus;
// import hobbiedo.global.status.SuccessStatus;
// import lombok.AllArgsConstructor;
// import reactor.core.publisher.Mono;
//
// @Component
// @AllArgsConstructor
// public class ChatWebSocketHandler implements WebSocketHandler {
//
// 	private final ChatService chatService;
// 	private final ObjectMapper objectMapper;
//
// 	@Override
// 	public Mono<Void> handle(WebSocketSession session) {
// 		return session.receive()
// 			.map(WebSocketMessage::getPayloadAsText)
// 			.flatMap(message -> {
// 				try {
// 					Map<String, Object> messageMap = objectMapper.readValue(message,
// 						objectMapper.getTypeFactory()
// 							.constructMapType(Map.class, String.class, Object.class));
// 					String type = (String)messageMap.get("type");
//
// 					if ("auth".equals(type)) {
// 						String uuid = (String)messageMap.get("uuid");
// 						if (uuid == null || uuid.isEmpty()) {
// 							return session.send(Mono.just(session.textMessage(
// 									toJson(BaseResponse.onFailure(ErrorStatus.NO_FIND_UUID))
// 								)))
// 								.then(
// 									session.close()); // Close the connection if UUID is not present
// 						}
// 						// Save UUID in session attributes
// 						session.getAttributes().put("uuid", uuid);
// 						return Mono.empty();
// 					}
//
// 					String uuid = (String)session.getAttributes().get("uuid");
// 					if (uuid == null || uuid.isEmpty()) {
// 						return session.send(Mono.just(session.textMessage(
// 							toJson(BaseResponse.onFailure(ErrorStatus.NO_FIND_UUID))
// 						))).then(session.close()); // Close the connection if UUID is not present
// 					}
//
// 					return switch (type) {
// 						case "send" -> {
// 							ChatSendDTO chatSendDTO = objectMapper.convertValue(messageMap,
// 								ChatSendDTO.class);
// 							yield chatService.sendChat(chatSendDTO, uuid)
// 								.then(session.send(Mono.just(session.textMessage(
// 									toJson(BaseResponse.onSuccess(SuccessStatus.CREATE_CHAT, null))
// 								))));
// 						}
// 						case "views" -> {
// 							Long crewId = Long.valueOf((Integer)messageMap.get("crewId"));
// 							yield chatService.getStreamChat(crewId, uuid)
// 								.map(chatStreamDTO -> BaseResponse.onSuccess(
// 									SuccessStatus.FIND_CHAT_CONTENT, chatStreamDTO))
// 								.flatMap(response -> session.send(
// 									Mono.just(session.textMessage(toJson(response)))));
// 						}
// 						case "update-unread" -> {
// 							LastChatTimeDTO lastChatTimeDTO = objectMapper.convertValue(messageMap,
// 								LastChatTimeDTO.class);
// 							yield chatService.updateLastReadAt(uuid, lastChatTimeDTO)
// 								.then(session.send(Mono.just(session.textMessage(
// 									toJson(BaseResponse.onSuccess(SuccessStatus.UPDATE_LAST_READ_AT,
// 										null))
// 								))));
// 						}
// 						case "get-unread-count" -> {
// 							Long crewIdForUnread = Long.valueOf((Integer)messageMap.get("crewId"));
// 							yield chatService.getUnreadCount(crewIdForUnread, uuid)
// 								.map(unreadCountDTO -> BaseResponse.onSuccess(
// 									SuccessStatus.FIND_UNREAD_COUNT, unreadCountDTO))
// 								.flatMap(response -> session.send(
// 									Mono.just(session.textMessage(toJson(response)))));
// 						}
// 						default -> session.send(Mono.just(session.textMessage(
// 							toJson(BaseResponse.onFailure(ErrorStatus.NO_MATCH_SOCKET_TYPE))
// 						)));
// 					};
// 				} catch (GlobalException e) {
// 					return session.send(Mono.just(session.textMessage(
// 						toJson(BaseResponse.onFailure(e.getStatus()))
// 					)));
// 				} catch (Exception e) {
// 					return Mono.error(e);
// 				}
// 			})
// 			.then();
// 	}
//
// 	private String toJson(Object object) {
// 		try {
// 			return objectMapper.writeValueAsString(object);
// 		} catch (Exception e) {
// 			throw new RuntimeException("Error converting object to JSON", e);
// 		}
// 	}
// }
