package hobbiedo.chat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import reactor.core.publisher.Mono;

@SpringBootTest
class ChatApplicationTests {

	public static void main(String[] args) {
		ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
		client.execute(URI.create("ws://localhost:8080/ws/chat"), session ->
			session.send(Mono.just(session.textMessage("{\"type\": \"auth\", \"uuid\": \"your-uuid\"}")))
				.thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).doOnNext(System.out::println))
				.then()
		).block();
	}

}
