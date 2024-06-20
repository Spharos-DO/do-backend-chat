package hobbiedo.chat.infrastructure.mvc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import hobbiedo.chat.domain.ChatLastStatus;

public interface ChatLastStatusRepository extends MongoRepository<ChatLastStatus, String> {

	@Query(value = "{ 'crewId': ?0, 'uuid': ?1 }", fields = "{ 'lastReadAt': 1, 'connectionStatus': 1}")
	Optional<ChatLastStatus> findByUuidAndCrewId(Long crewId, String uuid);

	@Query(value = "{ 'uuid': ?0 }", fields = "{ 'crewId': 1 }")
	List<ChatLastStatus> findByUuid(String uuid);

	void deleteByCrewIdAndUuid(long crewId, String uuid);
}
