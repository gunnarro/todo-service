package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "TODO_ITEM", schema = "todo")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @SequenceGenerator(name = "todos_gen", sequenceName = "todos_seq")
    @Column(name = "ID", nullable = false)
    private Long id;

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

    @Column(name = "FK_TODO_ID", nullable = false, insertable = false, updatable = false)
    private Long fkTodoId;

    @ManyToOne
    @JoinColumn(name = "FK_TODO_ID")
    private Todo todo;

}
