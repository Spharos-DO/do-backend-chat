<<<<<<< HEAD
// package hobbiedo.global.config;
//
// import java.util.HashMap;
// import java.util.Map;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.HandlerMapping;
// import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
// import org.springframework.web.reactive.socket.WebSocketHandler;
// import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//
// import hobbiedo.chat.socketHander.ChatWebSocketHandler;
//
// @Configuration
// public class WebSocketConfig {
//
// 	@Bean
// 	public HandlerMapping webSocketMapping(ChatWebSocketHandler chatWebSocketHandler) {
// 		Map<String, WebSocketHandler> map = new HashMap<>();
// 		map.put("/ws/chat", chatWebSocketHandler);
//
// 		SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
// 		handlerMapping.setOrder(10);
// 		handlerMapping.setUrlMap(map);
// 		return handlerMapping;
// 	}
//
// 	@Bean
// 	public WebSocketHandlerAdapter handlerAdapter() {
// 		return new WebSocketHandlerAdapter();
// 	}
// }
=======
package hobbiedo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import hobbiedo.chat.application.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final WebSocketChatHandler webSocketChatHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// endpoint 설정 : /api/v1/chat/{postId}
		// 이를 통해서 ws://localhost:9090/ws/chat 으로 요청이 들어오면 websocket 통신을 진행한다.
		// setAllowedOrigins("*")는 모든 ip에서 접속 가능하도록 해줌
		registry.addHandler(webSocketChatHandler, "/ws").setAllowedOrigins("*");
	}
}
>>>>>>> b989396 (eureka client 적요)
