package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Table(name = "TODO_ITEM", schema = "todo")
@Entity
@Audited
@AuditOverride(forClass=BaseEntity.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TodoItem extends BaseEntity {

    @Audited(withModifiedFlag = true)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Audited(withModifiedFlag = true)
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Audited(withModifiedFlag = true)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Audited(withModifiedFlag = true)
    @Column(name = "ACTION", nullable = false)
    private String action;

    @Audited(withModifiedFlag = true)
    @Column(name = "ASSIGNED_TO", nullable = false)
    private String assignedTo;

    @Column(name = "FK_TODO_ID", nullable = false)
    private Long fkTodoId;

    @Column(name = "PRIORITY")
    private Integer priority;
}
