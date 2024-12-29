package org.gunnarro.microservice.todoservice.endpoint;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {

    Authentication getAuthentication();

    String getLoggedInUser();
}
