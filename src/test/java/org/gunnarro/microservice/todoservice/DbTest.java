package org.gunnarro.microservice.todoservice;

import org.junit.jupiter.api.Test;

public class DbTest {

    @Test
    void setupH2() {
        H2Setup.initDatabaseUsingPlainJDBCWithFile();
    }
}
