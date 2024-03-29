package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.type.NumericBooleanConverter;

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
    @Column(name = "CATEGORY", nullable = false)
    private String category;

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

    /**
     * An item can only be assigned to one todo list.
     */
    @Column(name = "FK_TODO_ID", nullable = false)
    private Long fkTodoId;

    @Audited(withModifiedFlag = true)
    @Column(name = "PRIORITY", nullable = false)
    private String priority;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "APPROVAL_REQUIRED", nullable = false)
    private Boolean approvalRequired;

   // @Convert(converter = NumericBooleanConverter.class)
   // @Column(name = "APPROVED", nullable = false)
   // private Boolean approved;
}
