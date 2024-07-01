// package hobbiedo.global.config;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
// import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//
// import com.mongodb.ReadPreference;
// import com.mongodb.reactivestreams.client.MongoClient;
// import com.mongodb.reactivestreams.client.MongoClients;
//
// @Configuration
// public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {
//
// 	@Value("${spring.data.mongodb.uri}")
// 	private String mongoUri;
//
// 	@Override
// 	protected String getDatabaseName() {
// 		return "Chat";
// 	}
//
// 	@Bean
// 	@Override
// 	public MongoClient reactiveMongoClient() {
// 		return MongoClients.create(mongoUri);
// 	}
//
// 	@Bean
// 	public ReactiveMongoTemplate reactiveMongoTemplate() {
// 		ReactiveMongoTemplate reactiveMongoTemplate = new ReactiveMongoTemplate(
// 			reactiveMongoClient(), getDatabaseName());
// 		reactiveMongoTemplate.setReadPreference(ReadPreference.secondaryPreferred());
// 		return reactiveMongoTemplate;
// 	}
// }
