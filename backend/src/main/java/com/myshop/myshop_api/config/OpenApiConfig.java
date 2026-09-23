package com.myshop.myshop_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Metadados do documento OpenAPI exibido no Swagger UI
 * ({@code /swagger-ui.html}). Os endpoints em si são descobertos pelo springdoc
 * a partir dos controllers.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myShopOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("MyShop API")
                        .description("API de clientes, produtos, pedidos e pagamentos.")
                        .version("v1"))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local")));
    }
}
