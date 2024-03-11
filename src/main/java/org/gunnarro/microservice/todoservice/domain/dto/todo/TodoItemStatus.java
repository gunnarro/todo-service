package org.gunnarro.microservice.todoservice.domain.dto.todo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Holds all possible statues for a todo item")
public enum TodoItemStatus {
    OPEN("open"), IN_PROGRESS("inProgress"), ON_HOLD("onHold"), DONE("done"), CANCELLED("cancelled");

    private final String label;

    TodoItemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
