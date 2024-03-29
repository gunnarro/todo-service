package org.gunnarro.microservice.todoservice.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.gunnarro.microservice.todoservice.Utility;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "file:src/test/resources/it-application.properties")
public class TodoServiceApiValidationIT {

    // @LocalServerPort
    @Value("${server.port}")
    private int port;

    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;

    private RestClient restClient;

    private RestTemplate testRestTemplate;
    private HttpHeaders requestHeaders;

    @BeforeEach
    public void init() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        org.hibernate.mapping.Value v;
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_PATH", "config/server-identity-test.jks");
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_ALIAS", "gunnarro-microservice");
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_PASS", "test");
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");

        restClient = RestClient.builder()
                .baseUrl("")
                .defaultHeader(HttpHeaders.AUTHORIZATION, Utility.encodeBasic("my-service-name", "change-me"))
                .build();

        final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        final NoopHostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        final HttpClientConnectionManager cm = org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslsf)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        testRestTemplate = new RestTemplate(requestFactory);
        requestHeaders = Utility.createHeaders(username, password);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTodoServiceApiDocFromSwagger() throws IOException {
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(String.format("https://localhost:%s/v3/api-docs", port), HttpMethod.GET, entity, String.class);

        ResponseEntity<String> response2 = testRestTemplate.exchange(String.format("https://localhost:%s/v3/api-docs", port), HttpMethod.GET, entity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals(response2.getBody(), response.getBody());

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Object jsonObject = objectMapper.readValue(response.getBody(), Object.class);
        String todoServiceApiDoc = objectMapper.writeValueAsString(jsonObject);
      //  System.out.println(todoServiceApiDoc);

        BufferedWriter writer = new BufferedWriter(new FileWriter("docs/openapi/todo-service-api.yaml"));
        writer.write(todoServiceApiDoc);
        writer.close();
    }

}


