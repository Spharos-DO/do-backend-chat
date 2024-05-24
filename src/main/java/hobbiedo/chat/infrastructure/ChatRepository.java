package hobbiedo.chat.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hobbiedo.chat.domain.Chat;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
}
