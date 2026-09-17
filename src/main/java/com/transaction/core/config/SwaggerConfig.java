package com.transaction.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI transactionCore() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Core API")
                        .version("1.0.0")
                        .description("APIs for transaction processing"));
    }
}
