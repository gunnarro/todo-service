package org.gunnarro.microservice.todoservice.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.gunnarro.microservice.todoservice.Utility;
import org.gunnarro.microservice.todoservice.domain.dto.ErrorResponse;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_PATH", "config/server-identity-test.jks");
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_ALIAS", "gunnarro-microservice");
        //System.setProperty("SERVER_IDENTITY_KEYSTORE_PASS", "test");
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");

        objectMapper = new ObjectMapper();
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

        // NoopHostnameVerifier essentially turns hostname verification off
        //   CloseableHttpClient httpClient = HttpClientBuilder.create().(new NoopHostnameVerifier());.build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        testRestTemplate = new RestTemplate(requestFactory);
        //testRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        requestHeaders = Utility.createHeaders(username, password);
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

    @Disabled
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

        ResponseEntity<TodoDto> todoPostResponse = testRestTemplate.exchange(createURLWithPort("https", "todos"), HttpMethod.POST, new HttpEntity<>(todoDto, requestHeaders), TodoDto.class);

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
        assertEquals(38, todoPostResponse.getHeaders().get("UUID").toString().length());
        assertEquals(14, todoPostResponse.getHeaders().size());
        assertEquals("200 OK", todoPostResponse.getStatusCode().toString());

        assertNotNull(todoPostResponse.getBody().getId());
        assertEquals(todoDto.getName(), todoPostResponse.getBody().getName());
        assertEquals(todoDto.getDescription(), todoPostResponse.getBody().getDescription());
        assertEquals(todoDto.getStatus().name(), todoPostResponse.getBody().getStatus().name());
        assertEquals(todoDto.getCreatedByUser(), todoPostResponse.getBody().getCreatedByUser());
        assertEquals(todoDto.getLastModifiedByUser(), todoPostResponse.getBody().getLastModifiedByUser());
        assertEquals(todoDto.getCreatedDate(), todoPostResponse.getBody().getCreatedDate());
        assertEquals(todoDto.getLastModifiedByUser(), todoPostResponse.getBody().getLastModifiedByUser());
        assertEquals(0, todoPostResponse.getBody().getTodoItemDtoList().size());
        assertEquals(0, todoPostResponse.getBody().getParticipantDtoList().size());

        // update todo status
        TodoDto updateTodoDto = TodoDto.builder()
                .id(todoPostResponse.getBody().getId())
                .name(todoPostResponse.getBody().getName())
                .description(todoPostResponse.getBody().getDescription())
                .status(TodoStatus.IN_PROGRESS)
                .createdByUser(todoPostResponse.getBody().getCreatedByUser())
                .lastModifiedByUser("unittest")
                .todoItemDtoList(todoPostResponse.getBody().getTodoItemDtoList())
                .participantDtoList(todoPostResponse.getBody().getParticipantDtoList())
                .build();

        ResponseEntity<TodoDto> todoPutResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + updateTodoDto.getId()), HttpMethod.PUT, new HttpEntity<>(updateTodoDto, requestHeaders), TodoDto.class);
        assertEquals("200 OK", todoPutResponse.getStatusCode().toString());

        // add item to todo
        TodoItemDto todoItemDto1 = TodoItemDto.builder()
                .todoId(todoPostResponse.getBody().getId())
                .name("stuebord")
                .category("stue")
                .description("bord i stue")
                .status(TodoItemStatus.OPEN)
                .action(TaskAction.TO_BE_SOLD)
                .price(998)
                .approvalRequired(false)
                .priority(Priority.HIGHEST)
                .assignedTo("guro")
                .createdByUser("guro")
                .lastModifiedByUser("guro")
                .build();

        ResponseEntity<TodoItemDto> todoItemResponse1 = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoPostResponse.getBody().getId() + "/items"), HttpMethod.POST, new HttpEntity<>(todoItemDto1, requestHeaders), TodoItemDto.class);
        assertEquals("200 OK", todoItemResponse1.getStatusCode().toString());
        assertEquals(TodoItemStatus.OPEN, todoItemResponse1.getBody().getStatus());

        TodoItemDto todoItemDto2 = TodoItemDto.builder()
                .todoId(todoPostResponse.getBody().getId())
                .name("kjøleskap")
                .category("kjeller")
                .description("nope")
                .status(TodoItemStatus.OPEN)
                .action(TaskAction.TO_BE_SOLD)
                .price(2500)
                .approvalRequired(true)
                .priority(Priority.MEDIUM)
                .assignedTo("guro")
                .createdByUser("guro")
                .lastModifiedByUser("guro")
                .build();

        ResponseEntity<TodoItemDto> todoItemResponse2 = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoPostResponse.getBody().getId() + "/items"), HttpMethod.POST, new HttpEntity<>(todoItemDto2, requestHeaders), TodoItemDto.class);
        assertEquals("200 OK", todoItemResponse2.getStatusCode().toString());
        assertEquals(TodoItemStatus.OPEN, todoItemResponse2.getBody().getStatus());

        // get todo items
        ResponseEntity<TodoDto> todoGetResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoPostResponse.getBody().getId()), HttpMethod.GET, new HttpEntity<>(null, requestHeaders), TodoDto.class);
        todoGetResponse.getHeaders().forEach((k, v) -> System.out.println("Response Header: " + k + "=" + v));
        Assertions.assertEquals(HttpStatus.OK, todoGetResponse.getStatusCode());
        assertEquals(todoPostResponse.getBody().getId(), todoGetResponse.getBody().getId());
        assertEquals(2, todoGetResponse.getBody().getTodoItemDtoList().size());
        assertEquals(todoGetResponse.getBody().getId(), todoGetResponse.getBody().getTodoItemDtoList().get(0).getTodoId());
        assertNotNull(todoGetResponse.getBody().getTodoItemDtoList().get(0).getId());

        // update todo item status to done
        TodoItemDto updateTodoItemDto2 = TodoItemDto.builder()
                .id(todoItemResponse2.getBody().getId())
                .todoId(todoItemResponse2.getBody().getTodoId())
                .name(todoItemResponse2.getBody().getName())
                .category(todoItemResponse2.getBody().getCategory())
                .description(todoItemResponse2.getBody().getDescription())
                .status(TodoItemStatus.DONE)
                .action(todoItemResponse2.getBody().getAction())
                .price(todoItemResponse2.getBody().getPrice())
                .priority(Priority.MEDIUM)
                .assignedTo("guro")
                .createdByUser("guro")
                .lastModifiedByUser("guro")
                .build();

        ResponseEntity<TodoItemDto> updateTodoItemResponse2 = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoPostResponse.getBody().getId() + "/items"), HttpMethod.PUT, new HttpEntity<>(updateTodoItemDto2, requestHeaders), TodoItemDto.class);
        assertEquals("200 OK", updateTodoItemResponse2.getStatusCode().toString());
        assertEquals(TodoItemStatus.DONE, updateTodoItemResponse2.getBody().getStatus());

        // delete toto item
        ResponseEntity<TodoDto> todoItemDeleteResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/items/" + todoGetResponse.getBody().getTodoItemDtoList().get(0).getId()), HttpMethod.DELETE,  new HttpEntity<>(null, requestHeaders), TodoDto.class);
        assertEquals("204 NO_CONTENT", todoItemDeleteResponse.getStatusCode().toString());

        // check that item is deleted
        todoGetResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId()), HttpMethod.GET, new HttpEntity<>(null, requestHeaders), TodoDto.class);
        Assertions.assertEquals(HttpStatus.OK, todoGetResponse.getStatusCode());
        assertEquals(1, todoGetResponse.getBody().getTodoItemDtoList().size());

        // finally, clean up, delete created todo
        ResponseEntity<TodoDto> todoDeleteResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId()), HttpMethod.DELETE, new HttpEntity<>(null, requestHeaders), TodoDto.class);
        assertEquals("204 NO_CONTENT", todoDeleteResponse.getStatusCode().toString());

        // check todo audit history
        ParameterizedTypeReference<List<TodoHistoryDto>> todoHistoryResponseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoHistoryDto>> todoHistoryResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/history"), HttpMethod.GET, new HttpEntity<>(null, requestHeaders), todoHistoryResponseEntity);
        Assertions.assertEquals(HttpStatus.OK, todoHistoryResponse.getStatusCode());
        assertEquals(3, todoHistoryResponse.getBody().size());
        assertEquals(todoGetResponse.getBody().getId(), todoHistoryResponse.getBody().get(0).getId());
        assertEquals(null, todoHistoryResponse.getBody().get(0).getRevisionNumber());
        assertEquals("INSERT", todoHistoryResponse.getBody().get(0).getRevisionType());
        assertEquals("todo-crud-unit-test-all", todoHistoryResponse.getBody().get(0).getName());
        assertEquals("my todo list", todoHistoryResponse.getBody().get(0).getDescription());
        assertEquals("OPEN", todoHistoryResponse.getBody().get(0).getStatus());
        assertNotNull(todoHistoryResponse.getBody().get(0).getLastModifiedDate());
        assertNotNull(todoHistoryResponse.getBody().get(0).getCreatedDate());
        assertEquals("guro", todoHistoryResponse.getBody().get(0).getCreatedByUser());
        assertEquals("guro-2", todoHistoryResponse.getBody().get(0).getLastModifiedByUser());

        // che k todo item audit history
        ParameterizedTypeReference<List<TodoItemHistoryDto>> todoItemHistoryResponseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TodoItemHistoryDto>> todoItemHistoryResponse = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoGetResponse.getBody().getId() + "/items/" + todoItemResponse2.getBody().getId() + "/history"), HttpMethod.GET, new HttpEntity<>(null, requestHeaders), todoItemHistoryResponseEntity);
        Assertions.assertEquals(HttpStatus.OK, todoItemHistoryResponse.getStatusCode());
        assertEquals(3, todoItemHistoryResponse.getBody().size());
        assertEquals(todoGetResponse.getBody().getId(), todoItemHistoryResponse.getBody().get(0).getTodoId());
        assertEquals("kjøleskap", todoItemHistoryResponse.getBody().get(0).getName());

        //   HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
        //};
        // where root cause is 'Invalid CSRF token found for ..*, which means that POST id not supported, spring security use default csrf which do not support POST
//        ResponseEntity<TodoDto> response = testRestTemplate.exchange(createURLWithPort("https", "todos/" + UUID.randomUUID()), HttpMethod.POST, entity, TodoDto.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void todoParticipantCrud() {
        TodoDto todoDto = TodoDto.builder()
                .name("todo-test-participant")
                .status(TodoStatus.OPEN)
                .description("my todo list with participant")
                .createdByUser("guro")
                .lastModifiedByUser("guro-2")
                .build();

        HttpEntity<TodoDto> todoEntity = new HttpEntity<>(todoDto, requestHeaders);

        ResponseEntity<TodoDto> todoPostResponse = testRestTemplate.exchange(createURLWithPort("https", "todos"), HttpMethod.POST, todoEntity, TodoDto.class);
        assertEquals("200 OK", todoPostResponse.getStatusCode().toString());

        String todoId = todoPostResponse.getBody().getId();
        ParticipantDto participantDto = ParticipantDto.builder()
                .todoId(todoId)
                .name("guro")
                .email("guro@mail.org")
                .enabled(1)
                .build();

        // add participant to todo list
        HttpEntity<ParticipantDto> participantEntity = new HttpEntity<>(participantDto, requestHeaders);
        ResponseEntity<ParticipantDto> participantResp = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoId + "/participants"), HttpMethod.POST, participantEntity, ParticipantDto.class);
        assertEquals("200 OK", participantResp.getStatusCode().toString());
        assertEquals(todoId.toString(), participantResp.getBody().getTodoId());
        assertEquals("guro", participantResp.getBody().getName());
        assertEquals("guro@mail.org", participantResp.getBody().getEmail());

        // update existing participant
        ParticipantDto updateParticipantDto = ParticipantDto.builder()
                .id(participantResp.getBody().getId())
                .todoId(todoId)
                .name("guro")
                .email("guro@gmail.com")
                .enabled(1)
                .build();

        HttpEntity<ParticipantDto> updatedParticipantEntity = new HttpEntity<>(updateParticipantDto, requestHeaders);
        participantResp = testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoId + "/participants"), HttpMethod.PUT, updatedParticipantEntity, ParticipantDto.class);
        assertEquals("200 OK", participantResp.getStatusCode().toString());
        assertEquals(todoId, participantResp.getBody().getTodoId());
        assertEquals("guro", participantResp.getBody().getName());
        assertEquals("guro@gmail.com", participantResp.getBody().getEmail());

        // delete participant
        HttpEntity<ParticipantDto> participantDeleteEntity = new HttpEntity<>(null, requestHeaders);
        participantResp = testRestTemplate.exchange(createURLWithPort("https", "todos/" + participantResp.getBody().getTodoId() + "/participants/" + participantResp.getBody().getId()), HttpMethod.DELETE, participantDeleteEntity, ParticipantDto.class);
        assertEquals("204 NO_CONTENT", participantResp.getStatusCode().toString());

        // delete todo
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> responseDelete = testRestTemplate.exchange(createURLWithPort("https", "todos/" + todoId), HttpMethod.DELETE, entity, TodoDto.class);
        assertEquals("204 NO_CONTENT", responseDelete.getStatusCode().toString());
    }

    @Test
    void getTodoParticipants() {
        HttpEntity<ParticipantDto> requestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<ParticipantDto>> responseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ParticipantDto>> response = testRestTemplate.exchange(createURLWithPort("https", "todos/546769619193246584/participants"), HttpMethod.GET, requestEntity, responseEntity);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void addTodoParticipant_bad_request() throws JsonProcessingException {
        String todoId = "111111111";
        ParticipantDto participantDto = ParticipantDto.builder()
                .todoId(todoId)
                .name("guro")
                .email("guro@mail.org")
                .enabled(1)
                .build();

        // add participant to todo list
        HttpEntity<ParticipantDto> participantEntity = new HttpEntity<>(participantDto, requestHeaders);
        try {
            testRestTemplate.exchange(createURLWithPort("https", "/todos/" + todoId + "/participants"), HttpMethod.POST, participantEntity, ParticipantDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals("400 BAD_REQUEST", e.getStatusCode().toString());
            assertEquals("{\"httpStatus\":400,\"httpMessage\":\"Bad Request\",\"errorCode\":400200,\"description\":\"Service Input Validation Error\"}", e.getResponseBodyAsString());
            // check that a ErrorResponse is returned
            ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("400", errorResponse.getHttpStatus().toString());
            assertEquals("400200", errorResponse.getErrorCode().toString());
            assertEquals("Bad Request", errorResponse.getHttpMessage());
            assertEquals("Service Input Validation Error", errorResponse.getDescription());
        }
    }

    @Test
    void getTodoItemApprovals() {
        HttpEntity<ParticipantDto> requestEntity = new HttpEntity<>(null, requestHeaders);
        ParameterizedTypeReference<List<ApprovalDto>> responseEntity = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ApprovalDto>> response = testRestTemplate.exchange(createURLWithPort("https", "todos/546769619193246584/items/2222222/approvals"), HttpMethod.GET, requestEntity, responseEntity);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
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
        assertEquals("545716833011225412L", response.getBody().get(0).getId());
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

    @Test
    void deleteTodo() {
        HttpEntity<TodoDto> entity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<TodoDto> responseDelete = testRestTemplate.exchange(createURLWithPort("https", "todos/1"), HttpMethod.DELETE, entity, TodoDto.class);
        assertEquals("204 NO_CONTENT", responseDelete.getStatusCode().toString());
    }

    private String createURLWithPort(String protocol, String uri) {
        return String.format("%s://localhost:%s/todoservice/v1/%s", protocol, port, uri);
    }

    List<TodoDto> createTodoTestData() {
        Long b39TodoId = 2000L;
        Long stvgt35TodoId = 3000L;
        List<TodoItemDto> b39ToDoItemDtoList = List.of(createItem(b39TodoId, "tv", TodoItemStatus.IN_PROGRESS));
        List<TodoItemDto> stv35ToDoItemDtoList = List.of(createItem(stvgt35TodoId, "fryser", TodoItemStatus.IN_PROGRESS), createItem(stvgt35TodoId, "stol", TodoItemStatus.DONE));
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

    TodoItemDto createItem(Long todoId, String name, TodoItemStatus status) {
        return TodoItemDto.builder()
                .id(String.valueOf(new Random().nextLong()))
                .todoId(String.valueOf(todoId))
                .name(name)
                .description("stue")
                .action(TaskAction.TO_BE_SOLD)
                .status(status)
                .assignedTo("guro")
                .priority(Priority.MEDIUM)
                .approvalRequired(false)
                .build();
    }
}


