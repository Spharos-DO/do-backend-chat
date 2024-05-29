package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatUnReadStatus;
import reactor.core.publisher.Mono;

public interface ChatUnReadStatusRepository
	extends ReactiveMongoRepository<ChatUnReadStatus, String> {

	@Query(value = "{ 'uuid': ?0, 'crewId': ?1 }", fields = "{ 'lastAt': 1 }")
	Mono<ChatUnReadStatus> findByUuidAndCrewId(String uuid, Long crewId);

}
