package hobbiedo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = {
	@Server(url = "http://localhost:8080", description = "Local Test Server URL"),
	@Server(url = "https://nukfra.site/", description = "Deployment Test Server URL")})
@SpringBootApplication
@EnableDiscoveryClient
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
