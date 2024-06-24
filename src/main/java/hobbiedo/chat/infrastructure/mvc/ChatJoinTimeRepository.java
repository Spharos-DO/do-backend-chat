package hobbiedo.chat.infrastructure.mvc;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import hobbiedo.chat.domain.ChatJoinTime;

public interface ChatJoinTimeRepository extends MongoRepository<ChatJoinTime, String> {
	@Query(value = "{'uuid': ?0, 'crewId': ?1}", fields = "{'joinTime': 1}")
	Optional<Instant> findJoinTimeByUuidAndCrewId(String uuid, Long crewId);
}
