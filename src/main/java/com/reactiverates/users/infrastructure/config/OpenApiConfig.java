package com.reactiverates.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reactive Rates Users API")
                        .description("API для управления пользователями в системе Reactive Rates. " +
                                "Предоставляет полный набор операций CRUD для работы с пользователями, " +
                                "включая создание, чтение, обновление и удаление пользователей, " +
                                "а также поиск и фильтрацию по различным критериям.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Reactive Rates Team")
                                .email("support@reactiverates.com")
                                .url("https://reactiverates.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальная среда разработки"),
                        new Server()
                                .url("https://api.reactiverates.com")
                                .description("Продакшн сервер")
                ));
    }
}
