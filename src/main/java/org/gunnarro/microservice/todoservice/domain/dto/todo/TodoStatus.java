package org.gunnarro.microservice.todoservice.domain.dto.todo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Holds all possible statues for a todo task.")
@Getter
public enum TodoStatus {
    OPEN("open"), IN_PROGRESS("inProgress"), ON_HOLD("onHold"), DONE("done"), CANCELLED("cancelled");

    private final String label;

    TodoStatus(String label) {
        this.label = label;
    }

}
