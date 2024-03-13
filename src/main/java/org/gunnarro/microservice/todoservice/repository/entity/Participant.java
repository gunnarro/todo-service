package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Table(name = "TODO_PARTICIPANT", schema = "todo")
@Entity
@Audited
@AuditOverride(forClass=BaseEntity.class)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Participant extends BaseEntity {

    @Audited(withModifiedFlag = true)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Audited(withModifiedFlag = true)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FK_TODO_ID")
    private Long fkTodoId;

    @Audited(withModifiedFlag = true)
    @Column(name = "ENABLED", nullable = false)
    private Integer enabled;
}
