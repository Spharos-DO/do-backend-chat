package hobbiedo.global.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import hobbiedo.chat.kafka.dto.ChatLastStatusCreateDTO;
import hobbiedo.chat.kafka.dto.EntryExitDTO;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	private Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // 모든 패키지를 신뢰하도록 설정
		return props;
	}

	/**
	 * 게시글 생성,삭제 이벤트 컨슈머 팩토리 - 팀 점수 추가, 감소
	 * @return ConsumerFactory<String, CrewScoreDTO>
	 */
	@Bean
	public ConsumerFactory<String, ChatLastStatusCreateDTO> createLastStatusConsumerFactory() {

		Map<String, Object> configProps = consumerConfigs();
		return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
			new JsonDeserializer<>(ChatLastStatusCreateDTO.class, false));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ChatLastStatusCreateDTO>
		createLastStatusKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, ChatLastStatusCreateDTO> factory =
			new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(createLastStatusConsumerFactory());

		return factory;
	}

	/**
	 * 소모임 회원 입퇴장 채팅 알림 컨슈머 팩토리
	 * @return ConsumerFactory<String, EntryExitDTO>
	 */
	@Bean
	public ConsumerFactory<String, EntryExitDTO> sendEntryExitChatConsumerFactory() {

		Map<String, Object> configProps = consumerConfigs();
		return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
			new JsonDeserializer<>(EntryExitDTO.class, false));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, EntryExitDTO>
		sendEntryExitChatKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, EntryExitDTO> factory =
			new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(sendEntryExitChatConsumerFactory());

		return factory;
	}

}
