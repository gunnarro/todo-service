package org.gunnarro.microservice.todoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.endpoint.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    /**
     * Read logged-in user from spring security context.
     * read user from application.properties file:
     * spring.security.user.name=my-service-name
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(authenticationFacade.getLoggedInUser());
    }
}
