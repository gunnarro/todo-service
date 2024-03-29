package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

/**
 * withModifiedFlag = true: This cause that every tracked property has to have an accompanying boolean column in the schema
 * that stores information about the property's modifications.
 */
@Table(name = "TODO", schema = "todo")
@Entity
@Audited
@AuditOverride(forClass=BaseEntity.class)
//@AuditTable(value = "TODO_HISTORY")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Todo extends BaseEntity {

    @Audited(withModifiedFlag = true)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Audited(withModifiedFlag = true)
    @Column(name = "STATUS", nullable = false)
    //@Type(type=) can put enum type here
    private String status;

    @Audited(withModifiedFlag = true)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    /**
     * A todo list can have many todo items.
     */
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "FK_TODO_ID", updatable = false, insertable = false)
    @ToString.Exclude
    private List<TodoItem> todoItemList;

    /**
     * A todo list can have many participants.
     */
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = false)
    @JoinColumn(name = "FK_TODO_ID", updatable = false, insertable = false)
    @ToString.Exclude
    private List<Participant> participantList;

}
