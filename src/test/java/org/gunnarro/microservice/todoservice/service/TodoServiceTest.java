package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.gunnarro.microservice.todoservice.repository.TodoRepository;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.testcontainers.containers.MySQLContainer;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@SqlGroup({
        @Sql(statements = {
                "DROP TABLE IF EXISTS TODO;",
                "DROP TABLE IF EXISTS TODO_ITEM;"}),
        @Sql({"/db/schema-h2.sql", "/db/data-h2.sql"})
})
@DataJpaTest//excludeAutoConfiguration = {EnableAutoConfiguration.class})
//@ContextConfiguration(classes = {TodoService.class})
@ComponentScan(basePackages = {"org.gunnarro.microservice.todoservice"})
@TestPropertySource("/test-jpa-application.properties.disabled")
// opt uot the auto config of the test in-memory database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoServiceTest {

    // must have docker installed
    //@ClassRule
    //public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    private TodoRepository todoRepository;

    @Autowired
    TodoService toDoService;

    @BeforeAll
    static void beforeAll() {
      //  mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
       // mySQLContainer.stop();
    }

    @Test
    public void containerRunning(){
        //assertTrue(mySQLContainer.isRunning());
    }

    @Test
    void getTodoByUuid() {
        TodoDto todoDto = toDoService.getTodo(new Random().nextLong());
        assertEquals("", todoDto.getId());
    }

}
