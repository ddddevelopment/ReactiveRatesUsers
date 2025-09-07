package com.reactiverates.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server.dev.url}")
    private String devUrl;

    @Value("${openapi.server.dev.description}")
    private String devDescription;

    @Value("${openapi.server.prod.url}")
    private String prodUrl;

    @Value("${openapi.server.prod.description}")
    private String prodDescription;

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
                                .url(devUrl)
                                .description(devDescription),
                        new Server()
                                .url(prodUrl)
                                .description(prodDescription)
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Введите JWT токен в формате: Bearer <token>")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
