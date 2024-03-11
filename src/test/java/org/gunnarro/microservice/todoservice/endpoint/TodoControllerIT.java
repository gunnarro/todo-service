package org.gunnarro.microservice.todoservice.endpoint;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "file:src/test/resources/it-application.properties")
public class TodoControllerIT {

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
                .defaultHeader(HttpHeaders.AUTHORIZATION, encodeBasic("my-service-name", "change-me"))
                .build();

        final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        final NoopHostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        final HttpClientConnectionManager cm = org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslsf)
                .build();

        // NoopHostnameVerifier essentially turns hostname verification off
        //   CloseableHttpClient httpClient = HttpClientBuilder.create().(new NoopHostnameVerifier());.build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        testRestTemplate = new RestTemplate(requestFactory);
        //  testRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        requestHeaders = createHeaders(username, password);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    /**
     * https://dev.to/noelopez/new-restclient-in-spring-61-10ac
     */
    @Disabled
    @Test
    void getTodoById() {

        TodoDto todoDtoResponse = restClient.get()
                .uri(createURLWithPort("https", "todos/543855162824364902"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new NotFoundException(response.getStatusText());
                })
                .body(TodoDto.class);

        ResponseEntity<TodoDto> todoDtoResponseEntity = restClient.get()
                .uri(createURLWithPort("https", "todos/543855162824364902"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(TodoDto.class);
    }

   //@Disabled
    @Test
    void getTodoByRestTemplate() {
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> response = testRestTemplate.exchange(createURLWithPort("https", "todos/547357066032030645"), HttpMethod.GET, entity, TodoDto.class);
        response.getHeaders().forEach((k, v) -> System.out.println("Response Header: " + k + "=" + v));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(547357066032030645L, response.getBody().getId());
        assertEquals("b39", response.getBody().getName());
        assertEquals(1, response.getBody().getTodoItemDtoList().size());
        assertEquals(547357066032030645L, response.getBody().getTodoItemDtoList().get(0).getTodoId());
        assertEquals("trampoline", response.getBody().getTodoItemDtoList().get(0).getName());

        /*
        assertEquals("", response.getBody().getName());
        assertEquals("", response.getBody().getDescription());
        assertEquals("", response.getBody().getStatus());
        assertEquals("", response.getBody().getCreatedByUser());
        assertEquals("", response.getBody().getLastModifiedByUser());
        assertEquals("", response.getBody().getCreatedDate());
        assertEquals("", response.getBody().getLastModifiedByUser());
         */
    }

    @Test
    void todoAndItemsCrud() {
        TodoDto todoDto = TodoDto.builder()
                .name("todo-crud-unit-test-all")
                .status(TodoStatus.OPEN)
                .description("my todo list")
                //       .createdDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                //       .lastModifiedDate(LocalDateTime.of(2024, 2, 1, 10, 0, 0))
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();

        Assertions.assertEquals("[Basic bXktc2VydmljZS1uYW1lOmNoYW5nZS1tZQ==]", requestHeaders.get("Authorization").toString());
        Assertions.assertEquals("[application/json]", requestHeaders.get("Content-Type").toString());
        HttpEntity<TodoDto> todoEntity = new HttpEntity<>(todoDto, requestHeaders);

        ResponseEntity<TodoDto> todoPostResponse = testRestTemplate.exchange(createURLWithPort("https", "todos"), HttpMethod.POST, todoEntity, TodoDto.class);

        todoPostResponse.getHeaders().forEach((k, v) -> System.out.println("Response header: " + k + "=" + v));
        assertEquals("[Origin, Access-Control-Request-Method, Access-Control-Request-Headers]", todoPostResponse.getHeaders().get("Vary").toString());
        assertEquals("[0]", todoPostResponse.getHeaders().get("X-XSS-Protection").toString());
        assertEquals("[no-cache, no-store, max-age=0, must-revalidate]", todoPostResponse.getHeaders().get("Cache-Control").toString());
        assertEquals("[no-cache]", todoPostResponse.getHeaders().get("Pragma").toString());
        assertEquals("[0]", todoPostResponse.getHeaders().get("Expires").toString());
        assertEquals("[max-age=31536000 ; includeSubDomains]", todoPostResponse.getHeaders().get("Strict-Transport-Security").toString());
        assertEquals("[DENY]", todoPostResponse.getHeaders().get("X-Frame-Options").toString());
        assertEquals("[application/json]", todoPostResponse.getHeaders().get("Content-Type").toString());
        assertEquals("[chunked]", todoPostResponse.getHeaders().get("Transfer-Encoding").toString());
        //assertEquals(LocalDate.now(), response.getHeaders().get("Date").toString());
        assertEquals("[timeout=5]", todoPostResponse.getHeaders().get("Keep-Alive").toString());
        assertEquals("[keep-alive]", todoPostResponse.getHeaders().get("Connection").toString());
        assertEquals(13, todoPostResponse.getHeaders().size());
        assertEquals("200 OK", todoPostResponse.getStatusCode().toString());

        assertNotNull(todoPostResponse.getBody().getId());
        assertEquals(todoDto.getName(), todoPostResponse.getBody().getName());
        assertEquals(todoDto.getDescription(), todoPostResponse.getBody().getDescription());
        assertEquals(todoDto.getStatus().name(), todoPostResponse.getBody().getStatus());
        assertEquals(todoDto.getCreatedByUser(), todoPostResponse.getBody().getCreatedByUser());
        assertEquals(todoDto.getLastModifiedByUser(), todoPostResponse.getBody().getLastModifiedByUser());
        assertEquals(todoDto.getCreatedDate(), todoPostResponse.getBody().getCreatedDate());
        assertEquals(todoDto.getLastModifiedByUser(), todoPostResponse.getBody().getLastModifiedByUser());
        assertEquals(0, todoPostResponse.getBody().getTodoItemDtoList().size());

        // update todo status
        TodoDto updateTodoDto = TodoDto.builder()
                .id(todoPostResponse.getBody().getId())
                .name(todoPostResponse.getBody().getName())
                .description(todoPostResponse.getBody().getDescription())
                .status(TodoStatus.IN_PROGRESS)
                .createdByUser(todoPostResponse.getBody().getCreatedByUser())
                .lastModifiedByUser("unittest")
                .todoItemDtoList(todoPostResponse.getBody().getTodoItemDtoList())
                .build();
        HttpEntity<TodoDto> todoUpdateEntity = new HttpEntity<>(updateTodoDto, requestHeaders);
        ResponseEntity<TodoDto> todoPutResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + updateTodoDto.getId()), HttpMethod.PUT, todoUpdateEntity, TodoDto.class);
        assertEquals("200 OK", todoPutResponse.getStatusCode().toString());

        // add item to todo
        TodoItemDto todoItemDto1 = TodoItemDto.builder()
                .todoId(todoPostResponse.getBody().getId())
                .name("stuebord")
                .description("stue")
                .status("Open")
                .assignedTo("guro")
                .createdByUser("guro")
                .lastModifiedByUser("guro")
                .build();

        HttpEntity<TodoItemDto> todoItemEntity1 = new HttpEntity<>(todoItemDto1, requestHeaders);
        ResponseEntity<TodoItemDto> todoItemResponse1 = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoPostResponse.getBody().getId() + "/items"), HttpMethod.POST, todoItemEntity1, TodoItemDto.class);
        assertEquals("200 OK", todoItemResponse1.getStatusCode().toString());

        TodoItemDto todoItemDto2 = TodoItemDto.builder()
                .todoId(todoPostResponse.getBody().getId())
                .name("kjøleskap")
                .description("stue")
                .status("Open")
                .assignedTo("guro")
                .createdByUser("guro")
                .lastModifiedByUser("guro")
                .build();

        HttpEntity<TodoItemDto> todoItemEntity2 = new HttpEntity<>(todoItemDto2, requestHeaders);
        ResponseEntity<TodoItemDto> todoItemResponse2 = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoPostResponse.getBody().getId() + "/items"), HttpMethod.POST, todoItemEntity2, TodoItemDto.class);
        assertEquals("200 OK", todoItemResponse2.getStatusCode().toString());


        // get todo items
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> todoGetResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoPostResponse.getBody().getId()), HttpMethod.GET, entity, TodoDto.class);
        todoGetResponse.getHeaders().forEach((k, v) -> System.out.println("Response Header: " + k + "=" + v));
        Assertions.assertEquals(HttpStatus.OK, todoGetResponse.getStatusCode());
        assertEquals(todoPostResponse.getBody().getId(), todoGetResponse.getBody().getId());
        assertEquals(2, todoGetResponse.getBody().getTodoItemDtoList().size());
        assertEquals(todoGetResponse.getBody().getId(), todoGetResponse.getBody().getTodoItemDtoList().get(0).getTodoId());
        assertNotNull(todoGetResponse.getBody().getTodoItemDtoList().get(0).getId());

        // delete toto item
        HttpEntity<TodoItemDto> todoItemDeleteEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> todoItemDeleteResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/items/" + todoGetResponse.getBody().getTodoItemDtoList().get(0).getId()), HttpMethod.DELETE, todoItemDeleteEntity, TodoDto.class);
        assertEquals("204 NO_CONTENT", todoItemDeleteResponse.getStatusCode().toString());

        // check that item is deleted
        todoGetResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId()), HttpMethod.GET, entity, TodoDto.class);
        Assertions.assertEquals(HttpStatus.OK, todoGetResponse.getStatusCode());
        assertEquals(1, todoGetResponse.getBody().getTodoItemDtoList().size());

        // finally, clean up, delete created todo
        HttpEntity<TodoDto> todoDeleteEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> todoDeleteResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId()), HttpMethod.DELETE, todoDeleteEntity, TodoDto.class);
        assertEquals("204 NO_CONTENT", todoDeleteResponse.getStatusCode().toString());

        // check todo audit history
        HttpEntity<TodoHistoryDto> todoHistoryRequestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<TodoHistoryDto>> todoHistoryResponseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoHistoryDto>> todoHistoryResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/history"), HttpMethod.GET, todoHistoryRequestEntity, todoHistoryResponseEntity);
        Assertions.assertEquals(HttpStatus.OK, todoHistoryResponse.getStatusCode());
        assertEquals(3, todoHistoryResponse.getBody().size());
        assertEquals(todoGetResponse.getBody().getId(), todoHistoryResponse.getBody().get(0).getId());
        assertEquals(null, todoHistoryResponse.getBody().get(0).getRevisionNumber());
        assertEquals("INSERT", todoHistoryResponse.getBody().get(0).getRevisionType());
        assertEquals("todo-crud-unit-test-all", todoHistoryResponse.getBody().get(0).getName());
        assertEquals("my todo list", todoHistoryResponse.getBody().get(0).getDescription());
        assertEquals("Open", todoHistoryResponse.getBody().get(0).getStatus());
        assertNotNull(todoHistoryResponse.getBody().get(0).getLastModifiedDate());
        assertNotNull(todoHistoryResponse.getBody().get(0).getCreatedDate());
        assertEquals("guro", todoHistoryResponse.getBody().get(0).getCreatedByUser());
        assertEquals("guro-2", todoHistoryResponse.getBody().get(0).getLastModifiedByUser());

        // che k todo item audit history
        HttpEntity<TodoItemHistoryDto> todoItemHistoryRequestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<TodoItemHistoryDto>> todoItemHistoryResponseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoItemHistoryDto>> todoItemHistoryResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/items/" + todoItemResponse2.getBody().getId() + "/history"), HttpMethod.GET, todoItemHistoryRequestEntity, todoItemHistoryResponseEntity);
        Assertions.assertEquals(HttpStatus.OK, todoItemHistoryResponse.getStatusCode());
        assertEquals(2, todoItemHistoryResponse.getBody().size());
        assertEquals(todoGetResponse.getBody().getId(), todoItemHistoryResponse.getBody().get(0).getTodoId());
        assertEquals("kjøleskap", todoItemHistoryResponse.getBody().get(0).getName());

        //   HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
        //};
        // where root cause is 'Invalid CSRF token found for ..*, which means that POST id not supported, spring security use default csrf which do not support POST
//        ResponseEntity<TodoDto> response = testRestTemplate.exchange(createURLWithPort("https", "todos/" + UUID.randomUUID()), HttpMethod.POST, entity, TodoDto.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTodoAuditHistoryNotHit() {
        HttpEntity<TodoHistoryDto> requestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<TodoHistoryDto>> responseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoHistoryDto>> response = testRestTemplate.exchange(createURLWithPort("https", "todos/546769619193246584/history"), HttpMethod.GET, requestEntity, responseEntity);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Disabled
    @Test
    void getTodoAuditHistory() {
        HttpEntity<TodoHistoryDto> requestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<TodoHistoryDto>> responseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoHistoryDto>> response = testRestTemplate.exchange(createURLWithPort("https", "todos/546769619193246584/history"), HttpMethod.GET, requestEntity, responseEntity);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        assertEquals(545716833011225412L, response.getBody().get(0).getId());
        assertEquals(null, response.getBody().get(0).getRevisionNumber());
        assertEquals("INSERT", response.getBody().get(0).getRevisionType());
        assertEquals("todo-crud-unit-test-all", response.getBody().get(0).getName());
        assertEquals("my todo list", response.getBody().get(0).getDescription());
        assertEquals("Open", response.getBody().get(0).getStatus());
        assertNotNull(response.getBody().get(0).getLastModifiedDate());
        assertNotNull(response.getBody().get(0).getCreatedDate());
        assertEquals("guro", response.getBody().get(0).getCreatedByUser());
        assertEquals("guro-2", response.getBody().get(0).getLastModifiedByUser());
    }

    @Rollback
    @Test
    void deleteTodo() {
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> responseDelete = testRestTemplate.exchange(createURLWithPort("https", "todos/1"), HttpMethod.DELETE, entity, TodoDto.class);
        assertEquals("204 NO_CONTENT", responseDelete.getStatusCode().toString());
    }

    private String createURLWithPort(String protocol, String uri) {
        return String.format("%s://localhost:%s/todoservice/v1/%s", protocol, port, uri);
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
                set("Authorization", String.format("Basic %s", new String(encodedAuth)));
                set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                set("X-XSRF-TOKEN", "must be read from the response");
            }
        };
    }

    private String encodeBasic(String username, String password) {
        return "Basic " + Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
    }

    List<TodoDto> createTodoTestData() {
        Long b39TodoId = 2000L;
        Long stvgt35TodoId = 3000L;
        List<TodoItemDto> b39ToDoItemDtoList = List.of(createItem(b39TodoId, "tv", "Active"));
        List<TodoItemDto> stv35ToDoItemDtoList = List.of(createItem(stvgt35TodoId, "fryser", "Active"), createItem(stvgt35TodoId, "stol", "Finished"));
        return List.of(TodoDto.builder()
                        .name("B39")
                        .id(b39TodoId.toString())
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .status(TodoStatus.IN_PROGRESS)
                        .todoItemDtoList(b39ToDoItemDtoList)
                        .build(),
                TodoDto.builder()
                        .name("STV35")
                        .id(stvgt35TodoId.toString())
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .lastModifiedByUser("adm")
                        .status(TodoStatus.IN_PROGRESS)
                        .todoItemDtoList(stv35ToDoItemDtoList)
                        .build());
    }

    TodoItemDto createItem(Long todoId, String name, String status) {
        return TodoItemDto.builder()
                .id(String.valueOf(new Random().nextLong()))
                .todoId(String.valueOf(todoId))
                .name(name)
                .description("stue")
                .action("selges")
                .status(status)
                .assignedTo("guro")
                .build();
    }
}


