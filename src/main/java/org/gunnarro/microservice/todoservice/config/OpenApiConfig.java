package org.gunnarro.microservice.todoservice.config;

import com.sun.jdi.event.MethodEntryEvent;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PipedOutputStream;


/**
 * ref: <a href="https://springdoc.org/faq.html">Springdoc faq</a>
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Todo",
                description = "Rest Api for todo",
                version = "V1",
                contact = @Contact(
                        name = "gunnarro",
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

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi adminOpenApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/adminservice/v1/**")
                .displayName("Administration")
                .build();
    }

    @Bean
    public GroupedOpenApi todoOpenApi() {
        return GroupedOpenApi.builder()
                .group("todo")
                .pathsToMatch("/todoservice/v1/**")
                .displayName("Todo services")
                .build();
    }

}
