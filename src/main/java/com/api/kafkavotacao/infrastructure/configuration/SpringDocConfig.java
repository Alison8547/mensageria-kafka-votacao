package com.api.kafkavotacao.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI kafkaOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Kafka-Votação API")
                        .description("Documentaçao Kafka-Votação")
                        .version("v1.0.0"));
    }
}
