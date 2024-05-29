package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatUnReadStatus;
import reactor.core.publisher.Mono;

public interface ChatUnReadStatusRepository
	extends ReactiveMongoRepository<ChatUnReadStatus, String> {
	Mono<ChatUnReadStatus> findByUuidAndCrewId(String uuid, Long crewId);
}
