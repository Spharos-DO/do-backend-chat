package hobbiedo.chat.infrastructure;

import java.time.Instant;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import hobbiedo.chat.domain.Chat;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
	@Tailable
	@Query(value = "{ 'crewId' : ?0, 'createdAt' : { $gte: ?1 } }", fields = "{ 'id': 0 }")
	Flux<Chat> findChatByCrewIdAndCreatedAtOrAfter(Long crewId, Instant since);

	// @Query(value = "{ 'crewId': ?0, 'entryExitNotice': null }",
	// 	sort = "{ 'createdAt': -1 }", fields = "{ 'createdAt': 1 }")

	// @Aggregation(pipeline = {
	// 	"{ '$match': { 'crewId': ?0, 'entryExitNotice': null } }",
	// 	"{ '$sort': { 'createdAt': -1 } }",
	// 	"{ '$limit': 1 }"
	// })
	// Mono<Chat> findLatestByCrewId(Long crewId);
	// @Aggregation(pipeline = {
	// 	"{ '$match': { 'uuid': ?0 } }",
	// 	"{ '$sort': { 'createdAt': -1 } }",
	// 	"{ '$group': { '_id': '$crewId', 'latestChat': { '$first': '$$ROOT' } } }",
	// 	"{ '$project': { 'crewId': '$_id', 'createdAt': '$latestChat.createdAt' } }"
	// })
	// Flux<Chat> findLatestChatsByUuid(String uuid);

}
