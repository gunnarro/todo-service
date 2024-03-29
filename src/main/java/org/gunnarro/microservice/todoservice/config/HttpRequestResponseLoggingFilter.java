package org.gunnarro.microservice.todoservice.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * not in use
 */
@Slf4j
//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
public class HttpRequestResponseLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("Http Request  {} : {}", req.getMethod(), req.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("Http Response :{}, {}", res.getContentType(), res.getStatus());
    }
}
