package hobbiedo.chat.application.reactive;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UnreadCountService {
	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	private String getUnreadCountKey(long crewId, String uuid) {
		return String.format("unreadCount:%d:%s", crewId, uuid);
	}

	public Mono<Void> initializeUnreadCount(long crewId, String uuid, long count) {
		String key = getUnreadCountKey(crewId, uuid);
		return reactiveRedisTemplate.opsForValue().set(key, String.valueOf(count)).then();
	}

	public Mono<Void> incrementUnreadCount(long crewId, String uuid) {
		String key = getUnreadCountKey(crewId, uuid);
		return reactiveRedisTemplate.opsForValue().increment(key).then();
	}

	public Mono<Void> decrementUnreadCount(long crewId, String uuid) {
		String key = getUnreadCountKey(crewId, uuid);
		return reactiveRedisTemplate.opsForValue().decrement(key).then();
	}

	public Mono<Long> getUnreadCount(long crewId, String uuid) {
		String key = getUnreadCountKey(crewId, uuid);
		return reactiveRedisTemplate.opsForValue().get(key)
			.map(Long::valueOf)
			.switchIfEmpty(Mono.just(0L));
	}

	public Mono<Void> deleteUnreadCount(long crewId, String uuid) {
		String key = getUnreadCountKey(crewId, uuid);
		return reactiveRedisTemplate.opsForValue().delete(key).then();
	}

}
