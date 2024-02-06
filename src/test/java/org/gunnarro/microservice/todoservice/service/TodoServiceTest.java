package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.UUID;

@Disabled
@SqlGroup({
        @Sql(statements = {
                "DROP TABLE IF EXISTS TODO;",
                "DROP TABLE IF EXISTS TODO_ITEM;"}),
        @Sql({"/db/schema-h2.sql", "/db/data-h2.sql"})
})
@DataJpaTest(excludeAutoConfiguration = {EnableAutoConfiguration.class})
@ContextConfiguration(classes = {TodoService.class})
@ComponentScan(basePackages = {"org.gunnarro.microservice.mymicroservice"})
@TestPropertySource("/test-jpa-application.properties")
// opt uot the auto config of the test in-memory database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoServiceTest {

    private TodoRepository todoRepository;

    @Autowired
    TodoService toDoService;

    @Test
    void getTodoByUuid() {
        toDoService.getTodo(UUID.randomUUID().toString());
    }

}
