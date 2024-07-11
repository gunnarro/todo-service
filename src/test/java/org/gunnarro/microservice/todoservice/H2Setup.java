package org.gunnarro.microservice.todoservice;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2Setup {

    /**
     * Initialize H2 in-memory database using plain JDBC and SQL statements in a file.
     */
    public static void initDatabaseUsingPlainJDBCWithFile() {
        try (Connection conn = DriverManager.
                getConnection("jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'src/test/resources/db/todo-schema.sql';",
                        "admin",
                        "password")) {
            conn.createStatement().execute("insert into users (name, email) values ('Mike', 'mike@baeldung.com')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
