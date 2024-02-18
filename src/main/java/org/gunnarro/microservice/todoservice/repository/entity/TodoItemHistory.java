package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

@Table(name = "TODO_ITEM_HISTORY", schema = "todo")
@Entity
@Immutable
@Getter
@SuperBuilder
@NoArgsConstructor
public class TodoItemHistory extends AuditBaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ASSIGNED_TO", nullable = false)
    private String assignedTo;

    @Column(name = "FK_TODO_ID", nullable = false)
    private Long fkTodoId;

}
