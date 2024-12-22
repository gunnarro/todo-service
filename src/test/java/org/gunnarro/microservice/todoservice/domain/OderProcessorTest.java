package org.gunnarro.microservice.todoservice.domain;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OderProcessorTest {

    @Test
    void nextState() {
        OrderProcessor.OrderState ny = OrderProcessor.OrderState.NY;
        assertEquals(OrderProcessor.OrderState.VAREOVERFORING, ny.nextState());
    }
}
