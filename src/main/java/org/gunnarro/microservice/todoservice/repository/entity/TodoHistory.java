package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

@Table(name = "TODO_HISTORY", schema = "todo")
@Entity
@Immutable
@Getter
@SuperBuilder
@NoArgsConstructor
public class TodoHistory extends AuditBaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

}
