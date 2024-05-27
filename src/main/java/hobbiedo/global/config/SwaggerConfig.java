package hobbiedo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	private static final String BEARER_TOKEN = "Bearer ";
	private static final String BEARER_SCHEME = "bearer";
	private static final String BEARER_FORMAT = "JWT";

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().components(new Components())
			.addSecurityItem(new SecurityRequirement().addList(BEARER_TOKEN))
			.components(new Components().addSecuritySchemes(BEARER_TOKEN,
				new SecurityScheme().name(BEARER_TOKEN)
					.type(SecurityScheme.Type.HTTP)
					.scheme(BEARER_SCHEME)
					.bearerFormat(BEARER_FORMAT)))
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info().title("취미한다(DO) SpringDoc")
			.description("SpringDoc을 사용한 Swagger UI 테스트")
			.version("1.0.0");
	}
}
