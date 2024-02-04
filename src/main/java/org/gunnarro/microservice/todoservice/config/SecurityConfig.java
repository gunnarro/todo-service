package org.gunnarro.microservice.todoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


/**
 * Security configuration for Rest API and endpoints
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring Security 6 protects against CSRF attacks by default for unsafe HTTP methods,
     * such as a POST request, so no additional code is necessary <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html">spring security csrf</a>
     *
     * NOTE! Problem with “preflight” request which used HTTP OPTIONS, must skip authorization for such requests.
     *
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/api-docs/*", "/swagger-resources/**", "/swagger-ui.html", "/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                //.cors(Customizer.withDefaults()) // not needed, set as default.
                //.csrf(Customizer.withDefaults()) // not needed set as default.
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

  //  @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("/**"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type"));
        configuration.setExposedHeaders(List.of("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
