package org.gunnarro.microservice.todoservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * For CORS Http Headers see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Headers">preflight request</a>
 *
 * Security configuration for Rest API and endpoints
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring Security 6 protects against CSRF attacks by default for unsafe HTTP methods,
     * such as a POST request, so no additional code is necessary <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html">spring security csrf</a>
     * <p>
     * NOTE! Problem with “preflight” request which used HTTP OPTIONS, must skip authorization for such requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        .disable())
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                //        .cors(Customizer.withDefaults()) // not needed, set as default.
                //         .csrf(AbstractHttpConfigurer::disable) // not needed set as default. // fixme, default do not allow POST, PUT, DELETE
                //.csrf((csrf) -> csrf
                //        .disable()
                    //    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // http only because of java script
                    //    .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                //)
                //.sessionManagement().addSessionAuthenticationStrategy(SessionCreationPolicy.STATELESS)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * CORS configuration with Spring Security after bypassing pre-flight requests.
     * Since this server should be accessed from react client running at port 3000
     * @return
     */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        //     org.springframework.security.web.csrf.CsrfFilter csrf;
        CorsConfiguration configuration = new CorsConfiguration();
       // configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PATCH.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(), HttpMethod.HEAD.name()));
        configuration.setAllowCredentials(true);
        // The Access-Control-Allow-Headers response header is used in response to a preflight request
        configuration.setAllowedHeaders(List.of("authorization", "cache-control"));
      //  configuration.setExposedHeaders(List.of("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        log.info("use custom cors config.");
        return source;
    }
}
