package org.gunnarro.microservice.todoservice.domain.dto.todo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Holds all possible actions for a todo task.")
public enum TaskAction {
    TO_BE_SOLD("toBeSold"), GIVES_AWAY("givesAway"), THROW_AWAY("throwAway"), OWNED_BY("ownedBy"), GIVEN_TO("givenTo"), STAY_AS_IS("stayAsIs");

    private final String label;

    TaskAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
