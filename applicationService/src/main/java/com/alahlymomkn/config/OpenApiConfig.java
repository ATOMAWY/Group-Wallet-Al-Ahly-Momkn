package com.alahlymomkn.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI walletSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Group Wallet API")
                        .description("REST API for managing users, wallets, groups, and transactions for the Al Ahly Momkn Group Wallet system.")
                        .version("1.0.0"));
    }
}
