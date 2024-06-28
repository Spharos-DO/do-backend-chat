package hobbiedo.chat.application.reactive;

import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RedisPubSubService {

	private final ReactiveRedisTemplate<String, String> redisTemplate;

	public Mono<Void> publish(ChannelTopic topic, String message) {
		return redisTemplate.convertAndSend(topic.getTopic(), message).then();
	}

	public Flux<String> subscribeToChannel(String crewId) {
		return redisTemplate.listenToChannel(crewId)
			.map(ReactiveSubscription.Message::getMessage);
	}
}
