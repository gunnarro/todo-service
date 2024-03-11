package org.gunnarro.microservice.todoservice.domain.dto.todo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Holds all possible statues of a todo task")
public enum TodoStatus {
    OPEN("open"), IN_PROGRESS("inProgress"), ON_HOLD("onHold"), DONE("done"), CANCELLED("cancelled");

    private final String label;

    TodoStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
