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
	@Query(value = "{ 'crewId' : ?0, 'entryExitNotice': null, 'createdAt' : { $gt: ?1 } }",
		fields = "{ 'id': 0, 'uuid': 0 }")
	Flux<Chat> findByCrewIdAndCreatedAtAfter(Long crewId, Instant since);

	@Aggregation(pipeline = {
		"{ '$match': { 'crewId': ?0, 'entryExitNotice': null, 'createdAt': { '$gt': ?1 } } }",
		"{ '$sort': { 'createdAt': -1 } }",
		"{ '$limit': 1 }"
	})
	Mono<Chat> findLatestByCrewId(Long crewId, Instant joinTime);

	@Aggregation(pipeline = {
		"{ '$match': { 'crewId': ?0, 'uuid': ?1, 'entryExitNotice': { '$exists': true } } }",
		"{ '$sort': { 'createdAt': 1 } }",
		"{ '$limit': 1 }"
	})
	Mono<Chat> findFirstByCrewIdAndUuid(Long crewId, String uuid);

	@Query(value = "{ 'crewId': ?0, 'entryExitNotice': null, 'createdAt': { $gt: ?1, $lte: ?2 } }", count = true)
	Mono<Long> countByCrewIdAndCreatedAtBetween(Long crewId, Instant start, Instant end);

	// @Query(value = "{ 'crewId': ?0, 'entryExitNotice': null }",
	// 	sort = "{ 'createdAt': -1 }", fields = "{ 'createdAt': 1 }")

	// @Aggregation(pipeline = {
	// 	"{ '$match': { 'uuid': ?0 } }",
	// 	"{ '$sort': { 'createdAt': -1 } }",
	// 	"{ '$group': { '_id': '$crewId', 'latestChat': { '$first': '$$ROOT' } } }",
	// 	"{ '$project': { 'crewId': '$_id', 'createdAt': '$latestChat.createdAt' } }"
	// })
	// Flux<Chat> findLatestChatsByUuid(String uuid);

}
