package org.gunnarro.microservice.todoservice.endpoint;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements AuthenticationFacadeInterface {

    @Override
    public Authentication getAuthentication() {
        Authentication authentication = null;
        if (SecurityContextHolder.getContext() != null) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        return authentication;
    }

    /**
     * Method get logged in user credentials will fail, when called for users who
     * are not logged in.
     */
    @Override
    public String getLoggedInUser() {
        if (getAuthentication() != null && getAuthentication().getPrincipal() instanceof User) {
            return ((User) getAuthentication().getPrincipal()).getUsername();
        }
        throw new ArithmeticException("Not logged-in !");
    }
}
