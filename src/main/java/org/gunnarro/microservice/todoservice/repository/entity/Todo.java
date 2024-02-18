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
@ToString(callSuper = true, includeFieldNames=true)
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

   // @Column(name = "FK_USER_ACCOUNT_ID")
   // private Long userAccountId;
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_TODO_ID", updatable = false, insertable = false)
    @ToString.Exclude
    private List<TodoItem> todoItemList;

}
