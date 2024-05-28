package hobbiedo.chat.infrastructure;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import hobbiedo.chat.domain.Chat;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
	@Tailable
	@Query(value = "{ 'crewId' : ?0 }", fields = "{ 'id': 0, 'crewId': 0 }")
	Flux<Chat> findChatByCrewId(String crewId);

	@Tailable
	@Query("{ 'crewId' : ?0, 'createdAt' : { $gt: ?1 } }")
	Flux<Chat> findChatByCrewIdAndCreatedAtAfter(String crewId, LocalDateTime since);
}
