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

}
