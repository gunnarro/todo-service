package org.gunnarro.microservice.todoservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Todo Service",
                description = "Rest Api for todo services",
                version = "V1",
                contact = @Contact(
                        name = "gunnarro@gunnarroas.org",
                        url = "https://gunnarroas.org",
                        email = "gunnarro@gunnarroas.org"
                )),
        servers = @Server(url = "https://localhost:9999")
)
@SecurityScheme(
        name = "api",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)

public class OpenApiConfig {
}
