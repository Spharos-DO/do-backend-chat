package hobbiedo.chat.infrastructure.mvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import hobbiedo.chat.domain.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
	@Query("{ 'crewId': ?0, 'createdAt': { '$lt': ?1 } }")
	List<Chat> findLastChatByCrewId(Long crewId, Instant lastReadAt, Pageable pageable);

	@Query(value = "{ 'crewId': ?0, 'createdAt': { '$lt': ?1 } }", count = true)
	int countByCrewIdAndCreatedAtBefore(Long crewId, Instant lastReadAt);

	@Aggregation(pipeline = {
		"{ '$match': { 'crewId': ?0, 'entryExitNotice': null } }",
		"{ '$sort': { 'createdAt': -1 } }",
		"{ '$limit': 1 }"
	})
	Optional<Chat> findLastChatByCrewId(Long crewId);

	@Query(value = "{ 'crewId': ?0, 'createdAt': { '$gt': ?1 } }", count = true)
	long countByCrewIdAndCreatedAtAfter(Long crewId, Instant lastReadAt);

	@Query("{ 'crewId': ?0, 'imageUrl': { '$exists': true } }")
	List<Chat> findByCrewIdAndImageUrl(Long crewId);

	@Query(value = "{ 'imageUrl': { '$exists': true }, 'createdAt': { '$lt': ?0 } }", delete = true)
	void deleteByImageUrlExistsAndCreatedAtBefore(Instant expiryDate);
}
