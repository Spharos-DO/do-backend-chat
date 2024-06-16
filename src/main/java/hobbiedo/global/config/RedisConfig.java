package hobbiedo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {

	@Bean
	public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
		ReactiveRedisConnectionFactory connectionFactory) {
		return new ReactiveRedisTemplate<>(connectionFactory, RedisSerializationContext.string());
	}
}
