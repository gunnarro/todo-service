package org.gunnarro.microservice.todoservice.domain.dto.todo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Holds all possible priority levels for a todo item. Where default level is MEDIUM.")
public enum Priority {
    LOWEST("lowest"), LOW("low"), MEDIUM("medium"), HIGH("high"), HIGHEST("highest");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
