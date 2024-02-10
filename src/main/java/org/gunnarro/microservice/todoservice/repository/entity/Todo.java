package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Table(name = "TODO", schema = "todo")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Todo extends BaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "FK_TODO_ID")
    private List<TodoItem> todoItemList;

}
