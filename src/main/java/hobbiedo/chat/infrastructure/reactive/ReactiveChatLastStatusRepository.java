package hobbiedo.chat.infrastructure.reactive;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.ChatLastStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveChatLastStatusRepository
	extends ReactiveMongoRepository<ChatLastStatus, String> {

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }", fields = "{ 'lastReadAt': 1 }")
	Mono<ChatLastStatus> findLastReadAtByCrewIdAndUuid(Long crewId, String uuid);

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }")
	Mono<ChatLastStatus> findByCrewIdAndUuid(Long crewId, String uuid);

	@Query(value = "{ 'crewId': ?0, 'connectionStatus': false }", fields = "{ 'uuid': 1 }")
	Flux<ChatLastStatus> findByCrewIdAndConnectionStatusFalse(Long crewId);

	@Query(value = "{ 'uuid': ?0 }", fields = "{ 'crewId': 1 }")
	Flux<ChatLastStatus> findCrewIdsByUuid(String uuid);
}

