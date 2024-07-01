package hobbiedo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	@Override
	protected String getDatabaseName() {
		return "Chat";
	}

	@Bean
	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(mongoUri);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), getDatabaseName());
		mongoTemplate.setReadPreference(ReadPreference.secondaryPreferred());
		return mongoTemplate;
	}
}
