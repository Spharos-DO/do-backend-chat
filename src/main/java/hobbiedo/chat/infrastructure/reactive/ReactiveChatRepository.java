package hobbiedo.chat.infrastructure.reactive;

import java.time.Instant;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import hobbiedo.chat.domain.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveChatRepository extends ReactiveMongoRepository<Chat, String> {
	@Tailable
	@Query(value = "{ 'crewId' : ?0, 'createdAt' : { $gte: ?1 } }", fields = "{ 'id': 0 }")
	Flux<Chat> findByCrewIdAndCreatedAtOrAfter(Long crewId, Instant since);

	@Tailable
	@Query(value = "{ 'crewId' : ?0, 'entryExitNotice': { '$exists': false }, 'createdAt' : { $gte: ?1 } }",
		fields = "{ 'id': 0, 'uuid': 0 }")
	Flux<Chat> findByCrewIdAndCreatedAtAfter(Long crewId, Instant since);

	@Tailable
	@Aggregation(pipeline = {
		"{ '$match': { 'crewId': ?0, 'entryExitNotice': null } }",
		"{ '$sort': { 'createdAt': -1 } }",
		"{ '$limit': 1 }"
	})
	Flux<Chat> findLatestByCrewId(Long crewId);

	@Query(value = "{ 'crewId': ?0, 'createdAt': { $gte: ?1, $lt: ?2 } }", count = true)
	Mono<Long> countByCrewIdAndCreatedAtBetween(Long crewId, Instant start, Instant end);

}
