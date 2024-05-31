package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatUnReadStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatUnReadStatusRepository
	extends ReactiveMongoRepository<ChatUnReadStatus, String> {

	@Query(value = "{ 'uuid': ?0, 'crewId': ?1 }", fields = "{ 'lastReadAt': 1 }")
	Mono<ChatUnReadStatus> findByUuidAndCrewId(String uuid, Long crewId);

	// @Query(value = "{ 'uuid': ?0 }", fields = "{ 'crewId': 1 }")
	// Flux<ChatUnReadStatus> findByUuid(String uuid);

}
