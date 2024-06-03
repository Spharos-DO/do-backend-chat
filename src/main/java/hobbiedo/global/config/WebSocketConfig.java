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
