package hobbiedo.chat.infrastructure.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatJoinTime;
import reactor.core.publisher.Mono;

public interface ReactiveChatJoinTimeRepository extends
	ReactiveMongoRepository<ChatJoinTime, String> {
	Mono<ChatJoinTime> findByUuidAndCrewId(String uuid, Long crewId);
}
