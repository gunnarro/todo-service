package org.gunnarro.microservice.todoservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import org.gunnarro.microservice.todoservice.DefaultTestConfig;

@ContextConfiguration(classes = { MicrometerConfig.class, BuildProperties.class })
class MicrometerConfigTest extends DefaultTestConfig {

    @Autowired
    MicrometerConfig micrometerConfig;

    @MockBean
    MeterRegistry meterRegistryMock;

    @Test
    void micrometerConfigOk() {
        Assertions.assertNotNull(micrometerConfig);
    }

}
