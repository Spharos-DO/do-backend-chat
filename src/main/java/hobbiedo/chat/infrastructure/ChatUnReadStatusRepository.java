package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hobbiedo.chat.domain.ChatUnReadStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatUnReadStatusRepository
	extends ReactiveMongoRepository<ChatUnReadStatus, String> {

	@Query(value = "{ 'uuid': ?0, 'crewId': ?1 }", fields = "{ 'lastReadAt': 1 }")
	Mono<ChatUnReadStatus> findByUuidAndCrewId(String uuid, Long crewId);

	// @Query(value = "{ 'uuid': ?0 }", fields = "{ 'crewId': 1 }")
	// Flux<ChatUnReadStatus> findByUuid(String uuid);

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }")
	Mono<ChatUnReadStatus> findByCrewIdAndUuid(Long crewId, String uuid);
}
