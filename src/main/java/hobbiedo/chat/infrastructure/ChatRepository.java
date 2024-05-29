package hobbiedo.chat.infrastructure;

import java.time.Instant;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import hobbiedo.chat.domain.Chat;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
	@Tailable
	@Query(value = "{ 'crewId' : ?0, 'createdAt' : { $gte: ?1 } }", fields = "{ 'id': 0, 'crewId': 0 }")
	Flux<Chat> findChatByCrewIdAndCreatedAtOrAfter(Long crewId, Instant since);

	// @Tailable
	// @Query(value = "{ 'crewId' : ?0 }", fields = "{ 'id': 0, 'crewId': 0 }")
	// Flux<Chat> streamChatByCrewId(Long crewId);
}
