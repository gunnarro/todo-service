package org.gunnarro.microservice.todoservice.service;

import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.MountableFile;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * In order to run test containers test, you must have docker installed at local pc.
 */
@Disabled
@ComponentScan(basePackages = {"org.gunnarro.microservice.todoservice"})
//Testcontainers
public class TodoServiceTestContainerTest {

    // To enable reuse of containers, you must set 'testcontainers.reuse.enable=true' in a file located at /root/.testcontainers.properties
    // must have docker installed
    @ClassRule
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            //.withUsername("sys")
            //.withPassword("sys")
            //.withInitScript("db/todo-schema.sql") // inside src/test/resources
            //.withDatabaseName("todo")
            .withCopyFileToContainer(MountableFile.forClasspathResource("db/todo-schema.sql", 777), "/docker-entrypoint-initdb.d/todo-schema.sql" )
            .withReuse(true);

    @Autowired
    private TodoService toDoService;

    @BeforeAll
    static void beforeAll() {
       mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Test
    public void containerRunning(){
        assertTrue(mySQLContainer.isRunning());
    }

    @Test
    void getTodoByUuid() {

        TodoDto todoDto = toDoService.getTodo(new Random().nextLong());
        assertEquals("", todoDto.getId());
    }

}
