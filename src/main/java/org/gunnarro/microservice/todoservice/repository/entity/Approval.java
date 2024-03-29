package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.type.NumericBooleanConverter;

/*
*  A todo list can have many participants, but a participant is unique for each todo list.
 */
@Table(name = "TODO_ITEM_APPROVAL", schema = "todo")
@Entity
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Approval extends BaseEntity {

    @Column(name = "FK_TODO_PARTICIPANT_ID")
    private Long participantId;

    @Column(name = "FK_TODO_ITEM_ID")
    private Long todoItemId;

    @Audited(withModifiedFlag = true)
    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "APPROVED", nullable = false)
    private Boolean approved;
}
