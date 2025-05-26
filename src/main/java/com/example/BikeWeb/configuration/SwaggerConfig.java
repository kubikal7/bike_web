package com.example.BikeWeb.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("authHeader",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("authHeader"));
    }

    @Bean
    public OpenApiCustomizer hideAuthorizationHeader() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    for (Operation operation : pathItem.readOperations()) {
                        if (operation.getParameters() != null) {
                            operation.getParameters().removeIf(param ->
                                    "Authorization".equalsIgnoreCase(param.getName())
                                            && "header".equalsIgnoreCase(param.getIn()));
                        }
                    }
                });
            }
        };
    }
}