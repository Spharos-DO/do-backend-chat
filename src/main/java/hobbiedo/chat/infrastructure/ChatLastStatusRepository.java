package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatLastStatus;
import reactor.core.publisher.Mono;

public interface ChatLastStatusRepository
	extends ReactiveMongoRepository<ChatLastStatus, String> {

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }", fields = "{ 'lastReadAt': 1 }")
	Mono<ChatLastStatus> findLastReadAtByCrewIdAndUuid(Long crewId, String uuid);

	// @Query(value = "{ 'uuid': ?0 }", fields = "{ 'crewId': 1 }")
	// Flux<ChatUnReadStatus> findByUuid(String uuid);

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }")
	Mono<ChatLastStatus> findByCrewIdAndUuid(Long crewId, String uuid);
}
